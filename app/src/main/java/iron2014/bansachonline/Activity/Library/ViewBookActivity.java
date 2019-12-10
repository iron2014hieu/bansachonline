package iron2014.bansachonline.Activity.Library;

import android.Manifest;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;
import iron2014.bansachonline.nighmode_vanchuyen.SharedPref;

public class ViewBookActivity extends AppCompatActivity {
    SessionManager sessionManager;
    String linkBook;

    PDFView pdfView;
    ProgressBar progressBar;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        theme();
        setContentView(R.layout.activity_view_book);
        pdfView=findViewById(R.id.pdf_viewer);
        progressBar=findViewById(R.id.progressbar);
        sessionManager = new SessionManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar_viewpdf);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        HashMap<String, String> book = sessionManager.getLinkbook();
        linkBook = book.get(sessionManager.LINK_BOOK_READ);


        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new BaseMultiplePermissionsListener(){
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        super.onPermissionsChecked(report);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        super.onPermissionRationaleShouldBeShown(permissions, token);
                    }
                }).check();


        //load
                progressBar.setVisibility(View.VISIBLE);

                FileLoader.with(this)
                        .load(linkBook)
                        .fromDirectory("files", FileLoader.DIR_EXTERNAL_PUBLIC)
                        .asFile(new FileRequestListener<File>() {
                            @Override
                            public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                                progressBar.setVisibility(View.GONE);

                                File pdfFile = response.getBody();

                                pdfView.fromFile(pdfFile)
                                        .password(null).defaultPage(0).enableSwipe(true)
                                        .swipeHorizontal(false)
                                        .enableDoubletap(true)
                                        .onDraw(new OnDrawListener() {
                                            @Override
                                            public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                                            }
                                        }).onDrawAll(new OnDrawListener() {
                                    @Override
                                    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                                    }
                                }).onPageError(new OnPageErrorListener() {
                                    @Override
                                    public void onPageError(int page, Throwable t) {
                                        //Toast.makeText(ViewBookActivity.this, "Error while open page: "+page, Toast.LENGTH_SHORT).show();
                                    }
                                }).onPageChange(new OnPageChangeListener() {
                                    @Override
                                    public void onPageChanged(int page, int pageCount) {

                                    }
                                }).onTap(new OnTapListener() {
                                    @Override
                                    public boolean onTap(MotionEvent e) {
                                        return true;
                                    }
                                }).onRender(new OnRenderListener() {
                                    @Override
                                    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                                        pdfView.fitToWidth();
                                    }
                                })
                                        .enableAnnotationRendering(true)
                                        .invalidPageColor(Color.WHITE)
                                        .load();
                            }

                            @Override
                            public void onError(FileLoadRequest request, Throwable t) {
                                //Toast.makeText(ViewBookActivity.this, " Error :"+t.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("lỗi load pdf", t.getMessage());
                                progressBar.setVisibility(View.GONE);
                            }
                        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public  void theme(){
        if (sharedPref.loadNightModeState() == true){
            setTheme(R.style.darktheme);
        }else setTheme(R.style.AppTheme);
    }
    private void LoadBook(){

        progressBar.setVisibility(View.VISIBLE);
        FileLoader.with(this)
                .load("https://github.github.com/training-kit/downloads/github-git-cheat-sheet.pdf")
                .fromDirectory("PDFFiles", FileLoader.DIR_EXTERNAL_PUBLIC)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        progressBar.setVisibility(View.GONE);

                        File pdfFile = response.getBody();

                        pdfView.fromFile(pdfFile)
                                .password(null).defaultPage(0).enableSwipe(true)
                                .swipeHorizontal(false)
                                .enableDoubletap(true)
                                .onDraw(new OnDrawListener() {
                                    @Override
                                    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                                    }
                                }).onDrawAll(new OnDrawListener() {
                            @Override
                            public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                            }
                        }).onPageError(new OnPageErrorListener() {
                            @Override
                            public void onPageError(int page, Throwable t) {
                                //Toast.makeText(ViewBookActivity.this, "Error while open page: "+page, Toast.LENGTH_SHORT).show();
                            }
                        }).onPageChange(new OnPageChangeListener() {
                            @Override
                            public void onPageChanged(int page, int pageCount) {

                            }
                        }).onTap(new OnTapListener() {
                            @Override
                            public boolean onTap(MotionEvent e) {
                                return true;
                            }
                        }).onRender(new OnRenderListener() {
                            @Override
                            public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                                pdfView.fitToWidth();
                            }
                        })
                                .enableAnnotationRendering(true)
                                .invalidPageColor(Color.WHITE)
                                .load();


                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        //Toast.makeText(ViewBookActivity.this, "Eror:  "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
//package com.example.duan2muaban.Activity;
//
//import android.Manifest;
//import android.content.Intent;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import com.example.duan2muaban.MainActivity;
//import com.example.duan2muaban.R;
//import com.example.duan2muaban.Session.SessionManager;
//import com.github.barteksc.pdfviewer.PDFView;
//import com.github.barteksc.pdfviewer.listener.OnDrawListener;
//import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
//import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
//import com.github.barteksc.pdfviewer.listener.OnRenderListener;
//import com.github.barteksc.pdfviewer.listener.OnTapListener;
//import com.karumi.dexter.Dexter;
//import com.karumi.dexter.MultiplePermissionsReport;
//import com.karumi.dexter.PermissionToken;
//import com.karumi.dexter.listener.PermissionRequest;
//import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
//import com.krishna.fileloader.FileLoader;
//import com.krishna.fileloader.listener.FileRequestListener;
//import com.krishna.fileloader.pojo.FileResponse;
//import com.krishna.fileloader.request.FileLoadRequest;
//
//import java.io.File;
//import java.util.HashMap;
//import java.util.List;
//
//public class ViewBookActivity extends AppCompatActivity {
//    SessionManager sessionManager;
//    String linkBook;
//
//    PDFView pdfView;
//    ProgressBar progressBar;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_book);
//        Toolbar toolbar = findViewById(R.id.toolbar_viewpdf);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            }
//        });
//        pdfView=findViewById(R.id.pdf_viewer);
//        progressBar=findViewById(R.id.progressbar);
//
//        sessionManager = new SessionManager(this);
//
//        HashMap<String, String> book = sessionManager.getLinkbook();
//        linkBook = book.get(sessionManager.LINK_BOOK_READ);
//        setTitle("sach "+book.get(sessionManager.TENSACH));
//
//        Dexter.withActivity(this)
//                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .withListener(new BaseMultiplePermissionsListener(){
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        super.onPermissionsChecked(report);
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                        super.onPermissionRationaleShouldBeShown(permissions, token);
//                    }
//                }).check();
//
//        progressBar.setVisibility(View.VISIBLE);
//        if (linkBook!= null) {
//            FileLoader.with(this)
//                    .load(linkBook)
//                    .fromDirectory("PDFFiles", FileLoader.DIR_EXTERNAL_PUBLIC)
//                    .asFile(new FileRequestListener<File>() {
//                        @Override
//                        public void onLoad(FileLoadRequest request, FileResponse<File> response) {
//                            progressBar.setVisibility(View.GONE);
//
//                            File pdfFile = response.getBody();
//
//                            pdfView.fromFile(pdfFile)
//                                    .password(null).defaultPage(0).enableSwipe(true)
//                                    .swipeHorizontal(false)
//                                    .enableDoubletap(true)
//                                    .onDraw(new OnDrawListener() {
//                                        @Override
//                                        public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
//
//                                        }
//                                    }).onDrawAll(new OnDrawListener() {
//                                @Override
//                                public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
//
//                                }
//                            }).onPageError(new OnPageErrorListener() {
//                                @Override
//                                public void onPageError(int page, Throwable t) {
//                                    Toast.makeText(ViewBookActivity.this, "Error while open page: " + page, Toast.LENGTH_SHORT).show();
//                                }
//                            }).onPageChange(new OnPageChangeListener() {
//                                @Override
//                                public void onPageChanged(int page, int pageCount) {
//
//                                }
//                            }).onTap(new OnTapListener() {
//                                @Override
//                                public boolean onTap(MotionEvent e) {
//                                    return true;
//                                }
//                            }).onRender(new OnRenderListener() {
//                                @Override
//                                public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
//                                    pdfView.fitToWidth();
//                                }
//                            })
//                                    .enableAnnotationRendering(true)
//                                    .invalidPageColor(Color.WHITE)
//                                    .load();
//
//
//                        }
//
//                        @Override
//                        public void onError(FileLoadRequest request, Throwable t) {
//                            Toast.makeText(ViewBookActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//                        }
//                    });
//        }
//
//    }
//
//
//
//}
