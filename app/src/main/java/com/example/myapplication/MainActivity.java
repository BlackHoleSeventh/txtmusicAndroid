package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //计时器，7秒后仍没有赋予权限则退出
    private static int timeCount = 0;
    private int counter = 0;

    private static SoundPool soundpool;
    private static Map<String, Integer> soundmap = new HashMap<String, Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //设定调整音量为媒体音量,当暂停播放的时候调整音量就不会再默认调整铃声音量了
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //获取写权限，暂时没用
        /*
        requestWriteExternalPermission();
        while(!checkWrite()){
            if(timeCount <1){
                try {
                    timeCount++;
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                break;
                //exit();
            }
        }
        timeCount = 0;
        */

        initSoundPool();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void initSoundPool(){
        //当前系统的SDK版本大于等于21(Android 5.0)时
        if(Build.VERSION.SDK_INT > 21){
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入音频数量
            builder.setMaxStreams(5);
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适的属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);//STREAM_MUSIC
            //加载一个AudioAttributes
            builder.setAudioAttributes(attrBuilder.build());
            soundpool = builder.build();
        }else{
            soundpool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        soundmap.put("d1", soundpool.load(this, R.raw.d1, 1));
        soundmap.put("d2", soundpool.load(this, R.raw.d2, 1));
        soundmap.put("d3", soundpool.load(this, R.raw.d3, 1));
        soundmap.put("d4", soundpool.load(this, R.raw.d4, 1));
        soundmap.put("d5", soundpool.load(this, R.raw.d5, 1));
        soundmap.put("d6", soundpool.load(this, R.raw.d6, 1));
        soundmap.put("d7", soundpool.load(this, R.raw.d7, 1));

        soundmap.put("z1", soundpool.load(this, R.raw.z1, 1));
        soundmap.put("z2", soundpool.load(this, R.raw.z2, 1));
        soundmap.put("z3", soundpool.load(this, R.raw.z3, 1));
        soundmap.put("z4", soundpool.load(this, R.raw.z4, 1));
        soundmap.put("z5", soundpool.load(this, R.raw.z5, 1));
        soundmap.put("z6", soundpool.load(this, R.raw.z6, 1));
        soundmap.put("z7", soundpool.load(this, R.raw.z7, 1));

        soundmap.put("g1", soundpool.load(this, R.raw.g1, 1));
        soundmap.put("g2", soundpool.load(this, R.raw.g2, 1));
        soundmap.put("g3", soundpool.load(this, R.raw.g3, 1));
        soundmap.put("g4", soundpool.load(this, R.raw.g4, 1));
        soundmap.put("g5", soundpool.load(this, R.raw.g5, 1));
        soundmap.put("g6", soundpool.load(this, R.raw.g6, 1));
        soundmap.put("g7", soundpool.load(this, R.raw.g7, 1));

        soundmap.put("bd1", soundpool.load(this, R.raw.bd1, 1));
        soundmap.put("bd2", soundpool.load(this, R.raw.bd2, 1));
        soundmap.put("bd4", soundpool.load(this, R.raw.bd4, 1));
        soundmap.put("bd5", soundpool.load(this, R.raw.bd5, 1));
        soundmap.put("bd6", soundpool.load(this, R.raw.bd6, 1));

        soundmap.put("bz1", soundpool.load(this, R.raw.bz1, 1));
        soundmap.put("bz2", soundpool.load(this, R.raw.bz2, 1));
        soundmap.put("bz4", soundpool.load(this, R.raw.bz4, 1));
        soundmap.put("bz5", soundpool.load(this, R.raw.bz5, 1));
        soundmap.put("bz6", soundpool.load(this, R.raw.bz6, 1));

        soundmap.put("bg1", soundpool.load(this, R.raw.bg1, 1));
        soundmap.put("bg2", soundpool.load(this, R.raw.bg2, 1));
        soundmap.put("bg4", soundpool.load(this, R.raw.bg4, 1));
        soundmap.put("bg5", soundpool.load(this, R.raw.bg5, 1));
        soundmap.put("bg6", soundpool.load(this, R.raw.bg6, 1));

    }

    /**
     *
     */
    public void changeParam(){

        EditText t1 = (EditText) findViewById(R.id.editText);
        EditText t2 = (EditText) findViewById(R.id.editText2);

        ReadTxt.FILE_NAME = t1.getText().toString() + ".txt";
        try {
            ReadTxt.sleepTime = Integer.parseInt(t2.getText().toString());
        }catch (Exception e){
            ReadTxt.sleepTime = 500;
        }

        if(ReadTxt.sleepTime < 0 || ReadTxt.sleepTime > 5000){
            ReadTxt.sleepTime = 500;
        }
    }

    /**
     *
     * @param view
     */
    public void clickStart(View view){
        new Thread(){
            @Override
            public void run() {
                super.run();

                //获取读权限
                if(checkRead()){

                    changeParam();
                    try {
                        ReadTxt.conStart = true;
                        ReadTxt.start(soundpool, soundmap);
                    } catch (IOException e) {
                        //文件读取错误！出窗口提示！
                        ReadTxt.file = "";
                        e.printStackTrace();
                    }
                }else{
                    //获取读权限
                    requestReadExternalPermission();
                }

            }
        }.start();
    }

    public void clickStop(View view){
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    ReadTxt.conStart = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void clickPause(View view){
        String fileName = "";
        try {
            soundpool.autoPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickContinue(View view){
        String fileName = "";
        try {
            soundpool.autoResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /////////////////////////////////
    /////////////////////////////////
    /////////////////////////////////
    @SuppressLint("NewApi")
    private void requestReadExternalPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Debug", "READ permission IS NOT granted...");
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        } else {
            Log.d("Debug", "READ permission is granted...");
        }
    }

    @SuppressLint("NewApi")
    private void requestWriteExternalPermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Debug", "WRITE permission IS NOT granted...");
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            Log.d("Debug", "WRITE permission is granted...");
        }
    }

    @SuppressLint("NewApi")
    private void copyPropToZc(){
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            //copyStart();
        }
        else{

        }
    }

    @SuppressLint("NewApi")
    private boolean checkRead(){
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            return false;
        }
    }

    @SuppressLint("NewApi")
    private boolean checkWrite(){
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            return false;
        }
    }


    /**
     * 应用退出，结束所有的activity
     */
    public void exit() {
        this.finish();
        //释放内存，退出程序
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    /**
     * 按音量键的事件
     * @param keyCode
     * @param event
     * @return
     */
   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AudioManager am = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                am.adjustStreamVolume(AudioManager.STREAM_DTMF, AudioManager.ADJUST_RAISE, 0);
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                am.adjustStreamVolume(AudioManager.STREAM_DTMF, AudioManager.ADJUST_RAISE, 0);
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }*/

}
