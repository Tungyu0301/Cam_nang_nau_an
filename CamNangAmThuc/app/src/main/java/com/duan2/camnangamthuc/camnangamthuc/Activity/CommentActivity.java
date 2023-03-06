package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.ViewComment;
import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Comment;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import dmax.dialog.SpotsDialog;

public class CommentActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference commentlist;
    AlertDialog.Builder builder;
    AlertDialog b;
    AlertDialog dialogwaching;
    ProgressDialog pDialog;
    EditText inputcomment;
    FloatingActionButton fadsendcomment;
    String commentId = "";
    RecyclerView recyclerView;
  /*  RecyclerView.LayoutManager layoutManager;*/
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Comment, ViewComment> adapter;
    EditText editsendcomment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        commentlist = database.getReference("Comments");
        inputcomment = (EditText) findViewById(R.id.inputcomment);
        fadsendcomment = (FloatingActionButton) findViewById(R.id.fadsendcomment);
        fadsendcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postcomment();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.list_of_comment);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        //sap sep moi nhat
   /*    layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);*/

    /*    layoutManager.setReverseLayout(true);*/
        if (CheckInternet.haveNetworkConnection(this)) {
            //load dialog
            dialogwaching = new SpotsDialog(CommentActivity.this);
            dialogwaching.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    if (getIntent() != null) {
                        commentId = getIntent().getStringExtra("commentId");
                        if (!commentId.isEmpty()) {
                            loadComment(commentId);
                        }
                    }
                }
            };
            //set thời gian load dialog
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1500);
        } else {
            CheckInternet.ThongBao(this, "Vui lòng kết nối internet");
        }
    }

    private void loadComment(String commentId) {
        adapter = new FirebaseRecyclerAdapter<Comment, ViewComment>(Comment.class, R.layout.item_commnent_list, ViewComment.class,
                commentlist.orderByChild("commentId").equalTo(commentId)) {//tìm kiếm : select * from Food where emailusefood
            @Override
            protected void populateViewHolder(final ViewComment viewHolder, final Comment model, final int position) {
                viewHolder.comment_user.setText(model.getNameusecomment());
                viewHolder.comment_test.setText(model.getNamecomment());
                viewHolder.comment_time.setText(DateFormat.format("(HH:mm:ss) dd-MM-yyyy", model.getTimecomment()));
                Glide.with(getApplicationContext()).load(model.getImageusecomment()).apply(RequestOptions.circleCropTransform()).into(viewHolder.imgcomment);
                final Comment comment = model;
                viewHolder.detele_comment.setVisibility(View.INVISIBLE);
                viewHolder.edit_comment.setVisibility(View.INVISIBLE);
                //nếu như email của tài khoản giống với email của bình luận thì sẽ hiễn thị 2 chức năng xóa và sửa
                if (Common.userten.getEmail().equals(model.getEmailusecomment())) {
                    viewHolder.detele_comment.setVisibility(View.VISIBLE);
                    viewHolder.edit_comment.setVisibility(View.VISIBLE);
                    //button sửa
                    viewHolder.edit_comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            updatecomment(adapter.getRef(position).getKey(), adapter.getItem(position));
                        }
                    });

                    //buttom xóa
                    viewHolder.detele_comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String xoa = "Bạn có muốn xóa bình luận của <font color='green'> <Strong>" + "mình" + "</Strong></font> không";
                            AlertDialog.Builder dialogxoa = new AlertDialog.Builder(CommentActivity.this);
                            dialogxoa.setTitle("Xóa bình luận");
                            dialogxoa.setIcon(R.drawable.ic_deletestatus);
                            dialogxoa.setMessage(Html.fromHtml(xoa));
                            dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //lấy vị trí hiện tại
                                    deletecomment(adapter.getRef(position).getKey());
                                }
                            });
                            dialogxoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            dialogxoa.show();
                        }
                    });
                }
                //nếu như email của tài khoản giống với email của bài viết thì sẽ hiễn thị 2 chức năng xóa và sửa
                if (Common.userten.getEmail().equals(Common.communityten.getEmailusefood())) {
                    viewHolder.detele_comment.setVisibility(View.VISIBLE);
                    if (Common.userten.getEmail().equals(model.getEmailusecomment())) {
                        viewHolder.detele_comment.setVisibility(View.VISIBLE);
                        viewHolder.edit_comment.setVisibility(View.VISIBLE);
                        //button sửa
                        viewHolder.edit_comment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                updatecomment(adapter.getRef(position).getKey(), adapter.getItem(position));
                            }
                        });

                        //buttom xóa
                        viewHolder.detele_comment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String xoa = "Bạn có muốn xóa bình luận của <font color='green'> <Strong>" + "mình" + "</Strong></font> không";
                                AlertDialog.Builder dialogxoa = new AlertDialog.Builder(CommentActivity.this);
                                dialogxoa.setTitle("Xóa bình luận");
                                dialogxoa.setIcon(R.drawable.ic_deletestatus);
                                dialogxoa.setMessage(Html.fromHtml(xoa));
                                dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //lấy vị trí hiện tại
                                        deletecomment(adapter.getRef(position).getKey());
                                    }
                                });
                                dialogxoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                dialogxoa.show();
                            }
                        });
                        //ngược lại email của tài khoản không giống với email của bình luận thì sẽ ẩn 2 chức năng xóa và sửa
                    } else {
                        viewHolder.detele_comment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String xoa = "Bạn có muốn xóa bình luận của <font color='green'> <Strong>" + model.getNameusecomment() + "</Strong></font> không";
                                AlertDialog.Builder dialogxoa = new AlertDialog.Builder(CommentActivity.this);
                                dialogxoa.setTitle("Xóa bình luận");
                                dialogxoa.setIcon(R.drawable.ic_deletestatus);
                                dialogxoa.setMessage(Html.fromHtml(xoa));
                                dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //lấy vị trí hiện tại
                                        deletecomment(adapter.getRef(position).getKey());
                                    }
                                });
                                dialogxoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                dialogxoa.show();
                            }
                        });
                    }
                }
                //nếu thông tin so sánh bằng admin
                if(Common.userten.getRole().equalsIgnoreCase("admin")){
                    viewHolder.detele_comment.setVisibility(View.VISIBLE);
                    viewHolder.edit_comment.setVisibility(View.VISIBLE);
                    //truyền vào nếu email của tài khoản giống với emial của bình luận thì xóa sửa
                    if (Common.userten.getEmail().equals(model.getEmailusecomment())) {
                        //sửa
                        viewHolder.edit_comment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                updatecomment(adapter.getRef(position).getKey(), adapter.getItem(position));
                            }
                        });
                        //xóa
                        viewHolder.detele_comment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String xoa = "Bạn có muốn xóa bình luận của <font color='green'> <Strong>" + "mình" + "</Strong></font> không";
                                AlertDialog.Builder dialogxoa = new AlertDialog.Builder(CommentActivity.this);
                                dialogxoa.setTitle("Xóa bình luận");
                                dialogxoa.setIcon(R.drawable.ic_deletestatus);
                                dialogxoa.setMessage(Html.fromHtml(xoa));
                                dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //lấy vị trí hiện tại
                                        deletecomment(adapter.getRef(position).getKey());
                                    }
                                });
                                dialogxoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                dialogxoa.show();
                            }
                        });
                        //ngược lại chỉ có quyền xóa k có quyền sữa
                    }else {
                        //sửa
                        viewHolder.edit_comment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String xoa = "Bạn bạn không thể sửa bình luận của <font color='green'> <Strong>" + model.getNameusecomment() + "</Strong></font> vì đây là quyền riêng tư";
                                AlertDialog.Builder dialogsua = new AlertDialog.Builder(CommentActivity.this);
                                dialogsua.setTitle("Sửa bình luận");
                                dialogsua.setIcon(R.drawable.ic_edit_comment);
                                dialogsua.setMessage(Html.fromHtml(xoa));
                                dialogsua.setPositiveButton("Tôi đã hiểu", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //lấy vị trí hiện tại
                                    }
                                });
                                dialogsua.show();
                            }
                        });
                        //xóa
                        viewHolder.detele_comment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String xoa = "Bạn có muốn xóa bình luận của <font color='green'> <Strong>" + model.getNameusecomment() + "</Strong></font> không";
                                AlertDialog.Builder dialogxoa = new AlertDialog.Builder(CommentActivity.this);
                                dialogxoa.setTitle("Xóa bình luận");
                                dialogxoa.setIcon(R.drawable.ic_deletestatus);
                                dialogxoa.setMessage(Html.fromHtml(xoa));
                                dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //lấy vị trí hiện tại
                                        deletecomment(adapter.getRef(position).getKey());
                                    }
                                });
                                dialogxoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                dialogxoa.show();
                            }
                        });
                    }
                }
            }
        };
        //set adapter
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        dialogwaching.dismiss();
    }

    //cập nhật bình luận
    private void updatecomment(final String key, final Comment item) {
        builder = new AlertDialog.Builder(CommentActivity.this);
        builder.setTitle("Chỉnh sữa bình luận");
        LayoutInflater layoutInflater = CommentActivity.this.getLayoutInflater();
        final View updatecomment = layoutInflater.inflate(R.layout.item_edit_comment, null);
        editsendcomment = (EditText) updatecomment.findViewById(R.id.editsendcomment);
        builder.setView(updatecomment);
        builder.setIcon(R.drawable.ic_edit_comment);
        editsendcomment.setText(item.getNamecomment());
        final String name = Common.userten.getName();
        final String image = Common.userten.getImage();
        final long timecomment = new Date().getTime();
        builder.setPositiveButton("Chỉnh sửa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //lấy vị trí hiện tại
                item.setNameusecomment(name);
                item.setImageusecomment(image);
                item.setTimecomment(timecomment);
                item.setNamecomment(editsendcomment.getText().toString());
                commentlist.child(key).setValue(item);
                Toast.makeText(CommentActivity.this, "Chỉnh sửa thành công !!!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void deletecomment(String key) {
        commentlist.child(key).removeValue();
        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
    }

    //post comment
    private void postcomment(){
        pDialog = new ProgressDialog(CommentActivity.this);
        pDialog.setTitle("Đang đăng bài");
        pDialog.show();
        Comment comment = new Comment();
        String nameusecomment=  Common.userten.getName();
        String emailusecomment =Common.userten.getEmail();
        String imageusecomment =Common.userten.getImage();
        String namefoodcomment =Common.communityten.getNamefood();
        String commentid =Common.communityten.getId();
        String id = commentlist.push().getKey();
        long timecomment = new Date().getTime();
        comment.setId(id);
        comment.setNamecomment(inputcomment.getText().toString());
        comment.setNamefoodcomment(namefoodcomment);
        comment.setCommentId(commentid);
        comment.setNameusecomment(nameusecomment);
        comment.setEmailusecomment(emailusecomment);
        comment.setImageusecomment(imageusecomment);
        comment.setTimecomment(timecomment);
        if(comment !=null){
            commentlist.child(id).setValue(comment);
        }
        inputcomment.setText("");
        Toast.makeText(CommentActivity.this, "Cảm ơn bạn đã bình luận", Toast.LENGTH_SHORT).show();
        pDialog.dismiss();
    }
}
