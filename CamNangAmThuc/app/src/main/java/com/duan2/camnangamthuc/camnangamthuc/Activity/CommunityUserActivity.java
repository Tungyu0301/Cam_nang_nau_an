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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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
import com.duan2.camnangamthuc.camnangamthuc.Adapter.ViewCommunityUse;
import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.Community;
import com.duan2.camnangamthuc.camnangamthuc.Model.MenuHome;
import com.duan2.camnangamthuc.camnangamthuc.Model.Users;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class CommunityUserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemClickListener{
    ListView listViewMenu;
    ArrayList<MenuHome> listArray = new ArrayList<>();
    CustomView customView;
    Bitmap xemdanhgiaIcon,xemtaiveIcon,gopyIcon,huongdanIcon,doimatkhauIon,thongtintkIcon,dangxuatIcon,baivietdadangIcon;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Community,ViewCommunityUse> adapter;
    ProgressDialog pDialog;
    LinearLayout gvcamnang,gvcongdong;
    TextView txtviewcongdonguse;
    CircleImageView imglogincongdonguse;
    AlertDialog.Builder builder;
    AlertDialog b;
    Uri filePath;
    private final int PICK_IMAGE_REQUEST = 7171;
    ImageView imgViewadduse;
    EditText edttenmonanuse,edtnguyenlieuuse,edtcongthucuse;
    Community addCongDong;
    AlertDialog dialogwaching;
    FirebaseDatabase database;
    DatabaseReference congdonglist;
    String statusfood= "???? ph?? duy???t";
    boolean like = false;
/*    LinearLayoutManager layoutManager;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_user);
        database = FirebaseDatabase.getInstance();
        //B???c d??? li???u Json
        congdonglist = database.getReference("Communitys");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_congdonguser);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutcongdonguse);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewcongdonguse);
        navigationView.setNavigationItemSelectedListener(this);
        txtviewcongdonguse  = (TextView) findViewById(R.id.txtviewcongdonguse);
        imglogincongdonguse  = (CircleImageView)findViewById(R.id.imglogincongdonguse) ;
        txtviewcongdonguse.setText(Common.userten.getName());
        /*       Glide.with(getApplicationContext()).load(Common.userten.getImage()).into(imgloginuse);*/
        Glide.with(getApplicationContext()).load(Common.userten.getImage()).apply(RequestOptions.circleCropTransform()).into(imglogincongdonguse);
        Paper.init(this);
        //khai b??o listview menu
        thongtintkIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.thongtintaikhoang);
        listArray.add(new MenuHome("Th??ng tin t??i kho???n",thongtintkIcon));
        doimatkhauIon = BitmapFactory.decodeResource(this.getResources(),R.drawable.doimatkhau);
        listArray.add(new MenuHome("?????i m???t kh???u",doimatkhauIon));
        baivietdadangIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.baiviet);
        listArray.add(new MenuHome("B??i vi???t ???? ????ng",baivietdadangIcon));
        xemdanhgiaIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.danhgia);
        listArray.add(new MenuHome("Xem ????nh d???u",xemdanhgiaIcon));
        xemtaiveIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.taive);
        listArray.add(new MenuHome("Xem t???i v???",xemtaiveIcon));
        gopyIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.icongmail);
        listArray.add(new MenuHome("G??p ??",gopyIcon));
        huongdanIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.huongdan);
        listArray.add(new MenuHome("H?????ng d???n",huongdanIcon));
        dangxuatIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.dangxau);
        listArray.add(new MenuHome("????ng xu???t",dangxuatIcon));
        //th??m v??o adapter
        listViewMenu = (ListView) findViewById(R.id.listviewcongdonguse);
        customView = new CustomView(this,R.layout.dolistviewmenu,listArray);
        listViewMenu.setAdapter(customView);
        listViewMenu.setOnItemClickListener(this);
        //Load d??? li???u ra home
        recyclerView = (RecyclerView)findViewById(R.id.recycongdonguse);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(),
                R.anim.layout_fall);
        recyclerView.setLayoutAnimation(controller);
        gvcamnang =(LinearLayout)findViewById(R.id.gv_camnagusepaster);
        gvcamnang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(CommunityUserActivity.this,HomeUsers.class);
                startActivity(intent);

            }
        });
        gvcongdong =(LinearLayout)findViewById(R.id.gv_congdongusepaster);
        gvcongdong.setBackgroundResource(R.drawable.bachgrounk_item_list);
        gvcongdong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(CommunityUserActivity.this,CommunityUserActivity.class);
                startActivity(intent2);
            }
        });

        //s??? ki??n th??m
        FloatingActionButton fabaddcongdonguse = (FloatingActionButton) findViewById(R.id.fabaddcongdonguse);
        fabaddcongdonguse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(CommunityUserActivity.this);
                builder.setTitle("????ng b??i vi???t chia s???");
                builder.setMessage("L??u ?? h??nh ???nh c???a b???n chi???u d??i ph???i nh??? h??n ch??u r???ng|| k??ch c??? chu???n 500 tr??? l???i || kh??ng ph?? h???p k??ch th?????c s??? l??m x???u giao di???n");
                LayoutInflater layoutInflater = CommunityUserActivity.this.getLayoutInflater();
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
            dialogwaching = new SpotsDialog(CommunityUserActivity.this);
            dialogwaching.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                        if (!statusfood.isEmpty()) {
                            loadMenuCongdong(statusfood);
                        }
                }
            };
            //set th???i gian load dialog
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1500);
        }else {
            CheckInternet.ThongBao(this,"Vui l??ng k???t n???i internet");
        }
    }
    //l???y d??? li???u t??n v?? img ????? ra m??ng h??nh
    private void loadMenuCongdong(String statusfood) {
        adapter = new FirebaseRecyclerAdapter<Community,ViewCommunityUse>(Community.class,R.layout.item_viewcongdong,ViewCommunityUse.class,
                congdonglist.orderByChild("statusfood").equalTo(statusfood)){//t??m ki???m : select * from Food where emailusefood
            @Override
            protected void populateViewHolder(final ViewCommunityUse viewHolder, final Community model, final int position) {
                final String keylike = getRef(position).getKey();
                viewHolder.txtnamefoodcongdong.setText(model.getNamefood());
                viewHolder.txtnamefoodcongdong.setMaxLines(1);
                viewHolder.txtnamefoodcongdong.setEllipsize(TextUtils.TruncateAt.END);
                viewHolder.txtnamecongdonguse.setText(model.getNameusefood());
                viewHolder.likecountuse.setText(Integer.toString(model.likecount));
                viewHolder.txtngaydangcongdong.setText(DateFormat.format("???? ????ng:"+"(HH:mm:ss) dd-MM-yyyy", model.getTimefood()));
               Glide.with(getApplicationContext()).load(model.getImagefood()).into(viewHolder.viewimagecongdonguse);
                Glide.with(getApplicationContext()).load(model.getImageusefood()).apply(RequestOptions.circleCropTransform()).into(viewHolder.imageusecongdong);
                viewHolder.setColorLike(keylike);
                final Community community = model;
                viewHolder.setItemListener(new ItemClickListerner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodinfoIntent = new Intent(CommunityUserActivity.this,ViewPosterActivity.class);
                        Common.communityten = community;
                        //l???y id c???a Category l?? key,v?? v???y l???y key ????? ch??? item
                        foodinfoIntent.putExtra("StatusId",adapter.getRef(position).getKey());
                        startActivity(foodinfoIntent);
                    }
                });
                viewHolder.commentfood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent commentintent = new Intent(CommunityUserActivity.this,CommentActivity.class);
                        Common.communityten = model;
                        //l???y id c???a Category l?? key,v?? v???y l???y key ????? ch??? item
                        commentintent.putExtra("commentId",adapter.getRef(position).getKey());
                        startActivity(commentintent);
                    }
                });
                viewHolder.likefood.setOnClickListener(new View.OnClickListener() {
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
    private void AddCommunityfood() {
        final StorageReference storageReference;
        final FirebaseStorage storage;
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Communitys");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if (filePath != null) {
            pDialog = new ProgressDialog(CommunityUserActivity.this);
            pDialog.setTitle("??ang ????ng b??i");
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
                            String status = "??ang ch??? ph?? duy???t";
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
                            addCongDong.setLikecount(0);
                            if(addCongDong !=null){
                                reference.child(id).setValue(addCongDong);
                            }
                        }
                    });
                    String dang = "B???n ???? ????ng b??i <font color='red'> <Strong>"+ edttenmonanuse.getText().toString()+ "</Strong></font> th??nh c??ng v?? ??ang ch??? ph?? duy???t";
                    AlertDialog.Builder dialogxoa = new AlertDialog.Builder(CommunityUserActivity.this);
                    dialogxoa.setTitle("????ng b??i vi???t");
                    dialogxoa.setIcon(R.drawable.ic_status);
                    dialogxoa.setMessage(Html.fromHtml(dang));
                    dialogxoa.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //l???y v??? tr?? hi???n t???i
                        }
                    });
                    dialogxoa.setNegativeButton("Xem b??i", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intentxembai = new Intent(CommunityUserActivity.this,PostedarticleActivity.class);
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
                    Toast.makeText(CommunityUserActivity.this, "L???i "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("AAAA",e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double proga = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    pDialog.setMessage("Vui l??ng ?????i " + (int)proga + "%");
                }
            });
        }

    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Ch???n h??nh ???nh"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() !=null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgViewadduse.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutcongdonguse);
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
            Intent intent = new Intent(CommunityUserActivity.this,HomeAdmin.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.action_seach) {
            builder = new AlertDialog.Builder(CommunityUserActivity.this);
            builder.setTitle("Nh???p t??n m??n c???n t??m");
            builder.setMessage("Vi???t hoa ch??? c??i ?????u ti??n || Nh???p t??n m??n ph???i c?? d???u");
            LayoutInflater layoutInflater = CommunityUserActivity.this.getLayoutInflater();
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
                        editseach.setError("Vui l??ng nh???p t??n c???n tim");
                        editseach.requestFocus();
                        return;
                    } else {
                        Intent foodinfoIntent = new Intent(CommunityUserActivity.this, LoadSeachActivity.class);
                        //l???y id c???a Category l?? key,v?? v???y l???y key ????? ch??? item
                        foodinfoIntent.putExtra("KeyGet", seach);
                        startActivity(foodinfoIntent);
                        b.dismiss();
                    }
                }
            });
            return true;
        }
        if (id == R.id.action_folder) {
            Intent intent = new Intent(CommunityUserActivity.this,ViewListShopping.class);
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
    //send mail g??p ??
    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"buivietphii@gmail.com"};
        String[] CC = {"buivietphii@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:buivietphii@gmail.com"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Th?? G??p ??");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Xin ch??o nh?? ph??t tri???n App C???m Nang ???m Th???c");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CommunityUserActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    //s??? l?? s??? ki???n click cho listview Menu
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                Intent account = new Intent(CommunityUserActivity.this,AccountinformationActivity.class);
                account.putExtra("KeyEmail", Common.userten.getEmail());
                startActivity(account);
                break;
            case 1:
                builder = new AlertDialog.Builder(CommunityUserActivity.this);
                builder.setTitle("?????i m???t kh???u");
                builder.setMessage("Nh???p m???t kh???u");
                LayoutInflater layoutInflater = CommunityUserActivity.this.getLayoutInflater();
                final View viewdoimk = layoutInflater.inflate(R.layout.item_changpass_shaw,null);
                final EditText editmkcu = (EditText) viewdoimk.findViewById(R.id.editmkcu);
                final EditText editmkmoi = (EditText) viewdoimk.findViewById(R.id.editmkmoi);
                final EditText editmkmoinl = (EditText) viewdoimk.findViewById(R.id.editmkmoinl);
                final Button bntsendmk = (Button) viewdoimk.findViewById(R.id.btnsenddoimk);
                builder.setView(viewdoimk);
                builder.setIcon(R.drawable.ic_vpn_key_black_24dp);
                b = builder.create();
                b.show();
                bntsendmk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog dialogwaching = new SpotsDialog(CommunityUserActivity.this);
                        dialogwaching.show();
                        if(editmkcu.getText().toString().equals(Common.userten.getPassword())) {
                            if (editmkmoi.getText().toString().equals(editmkmoinl.getText().toString())) {
                                final Map<String, Object> doimatkhau = new HashMap<>();
                                doimatkhau.put("password", editmkmoi.getText().toString());
                                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            postSnapshot.getValue(Users.class);
                                            reference.child(postSnapshot.getKey()).updateChildren(doimatkhau)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            b.dismiss();
                                                            dialogwaching.dismiss();
                                                            Toast.makeText(CommunityUserActivity.this, "?????i m???t kh???u th??nh c??ng", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(CommunityUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                b.dismiss();
                                dialogwaching.dismiss();
                                Toast.makeText(CommunityUserActivity.this, "?????i m???t kh???u kh??ng th??nh c??ng", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            b.dismiss();
                            dialogwaching.dismiss();
                            Toast.makeText(CommunityUserActivity.this, "M???t kh???u kh??ng ch??nh x??t", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case 2:
                Intent status = new Intent(CommunityUserActivity.this,PostedarticleActivity.class);
                status.putExtra("StatusEmail", Common.userten.getEmail());
                startActivity(status);
                break;
            case 3:
                Intent favorite = new Intent(CommunityUserActivity.this,FavoriteViewActivity.class);
                startActivity(favorite);
                break;
            case 4:
                Intent intent2 = new Intent(CommunityUserActivity.this, ViewDownload.class);
                startActivity(intent2);
                break;
            case 5:
                sendEmail();
                break;
            case 6:
                Intent intent3 = new Intent(CommunityUserActivity.this, GuideViewActivity.class);
                startActivity(intent3);
                break;
            case 7:
                Paper.book().destroy();
                Intent longoutent = new Intent(CommunityUserActivity.this,CongDongActivity.class);
                longoutent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(longoutent);
                break;
        }
    }
}
