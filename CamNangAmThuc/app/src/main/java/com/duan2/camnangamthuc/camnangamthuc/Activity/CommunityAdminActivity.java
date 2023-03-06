package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.CustomView;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.ViewStatusAdmin;
import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.Community;
import com.duan2.camnangamthuc.camnangamthuc.Model.MenuHome;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class CommunityAdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemClickListener{
    ListView listViewMenu;
    ArrayList<MenuHome> listArray = new ArrayList<>();
    CustomView customView;
    Bitmap xemdanhgiaIcon, xemtaiveIcon, quanlitktkIcon, dangxuatIcon, baivietdadangIcon,thongtintkIcon,pheduyetIcon;
    ProgressDialog pDialog;
    LinearLayout gvcamnang, gvcongdong;
    TextView txtloginadmin;
    CircleImageView imgloginadmin;
    AlertDialog.Builder builder;
    EditText edttenmonanuse,edtnguyenlieuuse,edtcongthucuse;
    Uri filePath;
    private final int PICK_IMAGE_REQUEST = 7171;
    ImageView imgViewadduse;
    Community addCongDong;
    String statusfood= "Đã phê duyệt";
    AlertDialog dialogwaching;
    FirebaseDatabase database;
    DatabaseReference congdonglist;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Community,ViewStatusAdmin> adapter;
    boolean like = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_admin);
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        congdonglist = database.getReference("Communitys");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_congdongadmin);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutcongdongadmin);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewcongdongadmin);
        navigationView.setNavigationItemSelectedListener(this);
        txtloginadmin  = (TextView) findViewById(R.id.txtnamecongdongadmin);
        imgloginadmin  = (CircleImageView)findViewById(R.id.imglogincongdongadmin) ;
        txtloginadmin.setText(Common.userten.getName());
        /*Picasso.with(getBaseContext()).load(Common.userten.getImage()).into(imgloginad);*/
        Glide.with(getApplicationContext()).load(Common.userten.getImage()).apply(RequestOptions.circleCropTransform()).into(imgloginadmin);
        Paper.init(this);
        //khai báo listview menu
        quanlitktkIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.qltk);
        listArray.add(new MenuHome("Quản lý tài khoản", quanlitktkIcon));
        thongtintkIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.thongtintaikhoang);
        listArray.add(new MenuHome("Thông tin tài khoản",thongtintkIcon));
        baivietdadangIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.baiviet);
        listArray.add(new MenuHome("Bài viết đã đăng", baivietdadangIcon));
        xemdanhgiaIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.danhgia);
        listArray.add(new MenuHome("Xem đánh dấu", xemdanhgiaIcon));
        pheduyetIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.pheduyet);
        listArray.add(new MenuHome("Bài viết đang chờ", pheduyetIcon));
        xemtaiveIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.taive);
        listArray.add(new MenuHome("Xem tải về", xemtaiveIcon));
        dangxuatIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.dangxau);
        listArray.add(new MenuHome("Đăng xuất", dangxuatIcon));
        //thêm vào adapter
        listViewMenu = (ListView) findViewById(R.id.listviewmenucongdongadmin);
        customView = new CustomView(this, R.layout.dolistviewmenu, listArray);
        listViewMenu.setAdapter(customView);
        listViewMenu.setOnItemClickListener(this);
        //
        recyclerView = (RecyclerView)findViewById(R.id.recycongdongadmin);
        recyclerView.setHasFixedSize(true);
        //
        //sap sep moi nhat
       layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        gvcamnang = (LinearLayout) findViewById(R.id.gv_camnagadminpaster);
        gvcamnang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityAdminActivity.this, HomeAdmin.class);
                startActivity(intent);

            }
        });
        gvcongdong = (LinearLayout) findViewById(R.id.gv_congdongadminpaster);
        gvcongdong.setBackgroundResource(R.drawable.bachgrounk_item_list);
        gvcongdong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(CommunityAdminActivity.this, CommunityAdminActivity.class);
                startActivity(intent2);
            }
        });
        FloatingActionButton fabaddcongdongadmin = (FloatingActionButton) findViewById(R.id.fabaddcongdongadmin);
        fabaddcongdongadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(CommunityAdminActivity.this);
                builder.setTitle("Đăng bài viết chia sẽ");
                builder.setMessage("Lưu ý hình ảnh của bạn chiều dài phải nhỏ hơn chìu rộng|| kích cở chuẩn 500 trở lại ||  không phù hợp kích thước sẽ làm xấu giao diện");
                LayoutInflater layoutInflater = CommunityAdminActivity.this.getLayoutInflater();
                final View addfood = layoutInflater.inflate(R.layout.add_congdonguse, null);
                imgViewadduse= (ImageView) addfood.findViewById(R.id.imgViewadduse);
                edttenmonanuse = (EditText) addfood.findViewById(R.id.edttenmonanuse);
                edtnguyenlieuuse = (EditText) addfood.findViewById(R.id.edtnguyenlieuuse);
                edtcongthucuse = (EditText) addfood.findViewById(R.id.edtcongthucuse);
                final Button bntchonhinh = (Button) addfood.findViewById(R.id.iconphotoadduse);
                final Button bntdangbai = (Button) addfood.findViewById(R.id.btndangbaiviet);
                builder.setView(addfood);
                builder.setIcon(R.drawable.ic_food_add);
                final AlertDialog b = builder.create();
                b.show();
                bntchonhinh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chooseImage();
                    }
                });
                bntdangbai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AddCommunityfood();
                        b.dismiss();
                    }
                });
            }
        });
        if (CheckInternet.haveNetworkConnection(this)){
            //load dialog
            dialogwaching = new SpotsDialog(CommunityAdminActivity.this);
            dialogwaching.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    if (!statusfood.isEmpty()) {
                        loadMenuCongdong(statusfood);
                    }
                }
            };
            //set thời gian load dialog
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1500);
        }else {
            CheckInternet.ThongBao(this,"Vui lòng kết nối internet");
        }

    }
    //lấy dữ liệu tên và img đổ ra màng hình
    private void loadMenuCongdong(String statusfood) {
        adapter = new FirebaseRecyclerAdapter<Community,ViewStatusAdmin>(Community.class,R.layout.item_viewcongdong_admin,ViewStatusAdmin.class,
                congdonglist.orderByChild("statusfood").equalTo(statusfood)){//tìm kiếm : select * from Food where emailusefood
            @Override
            protected void populateViewHolder(ViewStatusAdmin viewHolder, final Community model, final int position) {
                final String keylike = getRef(position).getKey();
                viewHolder.txtnamefoodstatusadmin.setText(model.getNamefood());
                viewHolder.txtnamefoodstatusadmin.setMaxLines(1);
                viewHolder.txtnamefoodstatusadmin.setEllipsize(TextUtils.TruncateAt.END);
                viewHolder.txtnamestatusadmin.setText(model.getNameusefood());
                viewHolder.likecountadmin.setText(Integer.toString(model.likecount));
                viewHolder.txtngaydangstatusadmin.setText(DateFormat.format("Đã đăng:"+"(HH:mm:ss) dd-MM-yyyy", model.getTimefood()));
               Glide.with(getApplicationContext()).load(model.getImagefood()).into(viewHolder.imageviewstatusadmin);
                Glide.with(getApplicationContext()).load(model.getImageusefood()).apply(RequestOptions.circleCropTransform()).into(viewHolder.imageusestatusadmin);
                viewHolder.setColorLike(keylike);
                final Community community = model;
                viewHolder.setItemListener(new ItemClickListerner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodinfoIntent = new Intent(CommunityAdminActivity.this,ViewPosterActivity.class);
                        Common.communityten = community;
                        //lấy id của Category là key,vì vậy lấy key để chỉ item
                        foodinfoIntent.putExtra("StatusId",adapter.getRef(position).getKey());
                        startActivity(foodinfoIntent);
                    }
                });
                viewHolder.deletestatusadmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String xoa = "Bạn có muốn xóa bài viết <font color='green'> <Strong>"+model.getNamefood() + "</Strong></font> ra khỏi danh sách không";
                        AlertDialog.Builder dialogxoa = new AlertDialog.Builder(CommunityAdminActivity.this);
                        dialogxoa.setTitle("Xóa tài bài viết");
                        dialogxoa.setIcon(R.drawable.ic_deletestatus);
                        dialogxoa.setMessage(Html.fromHtml(xoa));
                        dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //lấy vị trí hiện tại
                                deletestatusadmin(adapter.getRef(position).getKey());
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
                viewHolder.commentfoodstatusadmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent commentintent = new Intent(CommunityAdminActivity.this,CommentActivity.class);
                        Common.communityten = model;
                        //lấy id của Category là key,vì vậy lấy key để chỉ item
                        commentintent.putExtra("commentId",adapter.getRef(position).getKey());
                        startActivity(commentintent);
                    }
                });
                viewHolder.likefoodstatusadmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        like = true;
                        congdonglist.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(like){
                                    int likecount =0;
                                    if(dataSnapshot.child(keylike).hasChild(Common.userten.getId())){
                                        congdonglist.child(keylike).child(Common.userten.getId()).removeValue();
                                        likecount = dataSnapshot.child(keylike).child("likecount").getValue(Integer.class);
                                        congdonglist.child(model.getId()).child("likecount").setValue(likecount-1);
                                        like = false;
                                    }else {
                                        congdonglist.child(keylike).child(Common.userten.getId()).setValue("Like");
                                        likecount = dataSnapshot.child(keylike).child("likecount").getValue(Integer.class);
                                        congdonglist.child(keylike).child("likecount").setValue(likecount+1);
                                        like = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        };
        //set adapter
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        dialogwaching.dismiss();
    }

    private void deletestatusadmin(String key) {
        //xóa vị trí đã lấy ra khỏi database
        congdonglist.child(key).removeValue();
        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
    }

    private void AddCommunityfood() {
        final StorageReference storageReference;
        final FirebaseStorage storage;
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Communitys");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if (filePath != null) {
            pDialog = new ProgressDialog(CommunityAdminActivity.this);
            pDialog.setTitle("Đang đăng bài");
            pDialog.show();
            final StorageReference imageFolder = storageReference.child("images/" + UUID.randomUUID().toString());
            imageFolder.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            addCongDong = new Community();
                            String nameusefood=  Common.userten.getName();
                            String emailusefood =Common.userten.getEmail();
                            String imageusefood =Common.userten.getImage();
                            String id = reference.push().getKey();
                            String status = "Đã phê duyệt";
                            long timefood = new Date().getTime();
                            addCongDong.setNamefood(edttenmonanuse.getText().toString());
                            addCongDong.setResourcesfood(edtnguyenlieuuse.getText().toString());
                            addCongDong.setRecipefood(edtcongthucuse.getText().toString());
                            addCongDong.setImagefood(uri.toString());
                            addCongDong.setNameusefood(nameusefood);
                            addCongDong.setEmailusefood(emailusefood);
                            addCongDong.setImageusefood(imageusefood);
                            addCongDong.setStatusfood(status);
                            addCongDong.setTimefood(timefood);
                            addCongDong.setId(id);
                            if(addCongDong !=null){
                                reference.child(id).setValue(addCongDong);
                            }
                        }
                    });
                    String dang = "Bạn đã đăng bài <font color='red'> <Strong>"+ edttenmonanuse.getText().toString()+ "</Strong></font> thành công";
                    AlertDialog.Builder dialogxoa = new AlertDialog.Builder(CommunityAdminActivity.this);
                    dialogxoa.setTitle("Đăng bài viết");
                    dialogxoa.setIcon(R.drawable.ic_status);
                    dialogxoa.setMessage(Html.fromHtml(dang));
                    dialogxoa.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //lấy vị trí hiện tại
                        }
                    });
                    dialogxoa.setNegativeButton("Xem bài", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intentxembai = new Intent(CommunityAdminActivity.this,PostedarticleActivity.class);
                            intentxembai.putExtra("StatusEmail", Common.userten.getEmail());
                            startActivity(intentxembai);
                        }
                    });
                    dialogxoa.show();
                    pDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pDialog.dismiss();
                    Toast.makeText(CommunityAdminActivity.this, "Lỗi "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("AAAA",e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double proga = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    pDialog.setMessage("Vui lòng đợi " + (int)proga + "%");
                }
            });
        }

    }
    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Chọn hình ảnh"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgViewadduse.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutcongdongadmin);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent intent = new Intent(CommunityAdminActivity.this, HomeAdmin.class);
            startActivity(intent);
            finish();
            return true;
        } if (id == R.id.action_seach) {
            builder = new AlertDialog.Builder(CommunityAdminActivity.this);
            builder.setTitle("Nhập tên món cần tìm");
            builder.setMessage("Viết hoa chữ cái đầu tiên || Nhập tên món phải có dấu");
            LayoutInflater layoutInflater = CommunityAdminActivity.this.getLayoutInflater();
            final View sendcode = layoutInflater.inflate(R.layout.item_seach, null);
            final EditText editseach = (EditText) sendcode.findViewById(R.id.edtseachname);
            final Button bntthoat = (Button) sendcode.findViewById(R.id.btn_thoat);
            final Button bnttim = (Button) sendcode.findViewById(R.id.btn_tim);
            builder.setView(sendcode);
            final AlertDialog b = builder.create();
            b.show();
            bntthoat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    b.dismiss();
                }
            });
            bnttim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String seach = editseach.getText().toString();
                    if (seach.isEmpty()) {
                        editseach.setError("Vui lòng nhập tên cần tim");
                        editseach.requestFocus();
                        return;
                    } else {
                        Intent foodinfoIntent = new Intent(CommunityAdminActivity.this, LoadSeachActivity.class);
                        //lấy id của Category là key,vì vậy lấy key để chỉ item
                        foodinfoIntent.putExtra("KeyGet", seach);
                        startActivity(foodinfoIntent);
                        b.dismiss();
                    }
                }
            });
            return true;
        }
        if (id == R.id.action_folder) {
            Intent intent = new Intent(CommunityAdminActivity.this,ViewListShopping.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //sử lý sự kiện click cho listview Menu
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                Intent listusead = new Intent(CommunityAdminActivity.this,ViewListUseAdminActivity.class);
                startActivity(listusead);
                break;
            case 1:
                Intent account = new Intent(CommunityAdminActivity.this,AccountinformationActivity.class);
                account.putExtra("KeyEmail", Common.userten.getEmail());
                startActivity(account);
                break;
            case 2:
                Intent status = new Intent(CommunityAdminActivity.this,PostedarticleActivity.class);
                status.putExtra("StatusEmail", Common.userten.getEmail());
                startActivity(status);
                break;
            case 3:
                Intent favorite = new Intent(CommunityAdminActivity.this,FavoriteViewActivity.class);
                startActivity(favorite);
                break;
            case 4:
                Intent approved = new Intent(CommunityAdminActivity.this,StatusApprovedActivity.class);
                startActivity(approved);
                break;
            case 5:
                Intent intent2 = new Intent(CommunityAdminActivity.this, ViewDownload.class);
                startActivity(intent2);
                break;
            case 6:
                Paper.book().destroy();
                Intent longoutent = new Intent(CommunityAdminActivity.this,Home.class);
                longoutent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(longoutent);
                break;
        }
    }
}
