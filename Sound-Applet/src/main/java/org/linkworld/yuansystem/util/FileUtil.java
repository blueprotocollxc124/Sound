package org.linkworld.yuansystem.util;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/3/1
 */


import lombok.extern.slf4j.Slf4j;
import org.linkworld.yuansystem.factory.SpringJobBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;

@Slf4j
@Component
public class FileUtil {

/*

  @Autowired
  static Environment environment;

  */

 public static String inAndOutFile(MultipartFile file, String fileName) {
  InputStream fileInputStream = null;
  FileOutputStream fileOutputStream = null;
  String absoluteFileName = null;
  String outFileName = null;
  File outFile = null;
  try {
   //outFileName = "D:\\IDEA__project\\Backend\\Sound-System\\src\\main\\resources\\static\\work\\";
   outFileName = File.separator+"opt"+File.separator+"jar"+File.separator+"work"+File.separator;
   String[] split = fileName.split("-");
  File studentFile = new File(outFileName + split[0]);
   if(!studentFile.exists()) {
    studentFile.mkdir();
   }
   absoluteFileName = fileName + ".mp3";
   if(fileName.contains("exec")) {
    File studentExecFile = new File(outFileName + split[0] + File.separator+"exec");
    if(!studentExecFile.exists()) {
     studentExecFile.mkdir();
    }
    outFile = new File(outFileName+File.separator+split[0]+File.separator+"exec"+File.separator+absoluteFileName);
   }else {
    outFile = new File(outFileName+File.separator+split[0]+File.separator+absoluteFileName);
   }
   fileInputStream = file.getInputStream();
   fileOutputStream= new FileOutputStream(outFile);
   int len = 0;
   byte[] bytes = new byte[1024];
   while ((len=fileInputStream.read(bytes))!=-1) {
    fileOutputStream.write(bytes,0,len);
   }
   if(len==-1) {
    log.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE,"文件传输成功"));
   }
  } catch (IOException e) {
   e.printStackTrace();
  } finally {
   try {
    if(fileInputStream!=null) {
     fileInputStream.close();
    }
   } catch (IOException e) {
    e.printStackTrace();
   }
   try {
    if(fileOutputStream!=null ) {
     fileOutputStream.close();

    }
   } catch (IOException e) {
    e.printStackTrace();
   }
  }
  return outFile.getAbsolutePath();
 }



}
