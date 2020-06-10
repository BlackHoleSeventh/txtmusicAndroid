package com.example.myapplication;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReadTxt {
    public static String FILE_NAME = "abc500.txt";
    private final static String PATH_NAME="/storage/emulated/0/txtmusic/";

    private static ReadTxt r = new ReadTxt();
    //级别
    private static String level = "z";
    //类型
    private static String type = "";

    public static int sleepTime = 500;

    public static String file = "";

    public static boolean conStart = true;

    static {
        file = "";
    }

    /**
     * 从sd card文件中读取数据
     * https://blog.csdn.net/yoryky/article/details/78675373
     * @param soundpool
     * @param soundmap
     * @return
     * @throws IOException
     */
    public static void start(SoundPool soundpool, Map<String, Integer> soundmap) throws IOException {

            //读取文件
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

                String filename = PATH_NAME + FILE_NAME;

                Log.i("debug","文件名为: "+filename);
                //打开文件输入流
                FileInputStream inputStream = new FileInputStream(filename);

                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                try {
                    StringBuffer sb = new StringBuffer();
                    String str ;
                    while ( (str = br.readLine())!= null){
                        sb.append(str);
                        sb.append("\r");
                        //System.out.println(s);
                    }
                    file = sb.toString();

                    //关闭输入流
                    br.close();
                    inputStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



        startPlay(soundpool, soundmap);

    }

    public static void startPlay(SoundPool soundpool, Map<String, Integer> soundmap){

        //播放文件
        char[] c = file.toCharArray();
        for(char tc : c){
            if(conStart) {
                choose(String.valueOf(tc), soundpool, soundmap);
            }else{
                break;
            }
        }
    }

    public static void choose(String judge, SoundPool soundpool, Map<String, Integer> soundmap){

        if("#".contains(judge)){
            type = "b";
            return;
        }

        if("\r".contains(judge)){
            return;
        }

        if(" ".contains(judge)){
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        if("1234567".contains(judge)){
            play(judge, soundpool, soundmap);
            type = "";
        }else if("(".contains(judge)){
            level = "d";
        }else if(")".contains(judge)){
            level = "z";
        }else if("[".contains(judge)){
            level = "g";
        }else if("]".contains(judge)){
            level = "z";
        }else{
            //否则不处理
        }

    }

    //播放
    public static void play(String num, SoundPool soundpool, Map<String, Integer> soundmap){
        StringBuffer musicUrl = new StringBuffer();

        musicUrl.append(type);
        musicUrl.append(level);
        musicUrl.append(num);

        //System.out.println(musicUrl.toString());
        playMusic(musicUrl.toString(), soundpool, soundmap);
    }

    /**
     * 可以播放wav格式的文件
     * @param url
     * @param soundpool
     * @param soundmap
     */
    public static void playMusic(String url, SoundPool soundpool, Map<String, Integer> soundmap) {

        soundpool.play(soundmap.get(url), 1, 1, 0, 0, 1);

    }

}
