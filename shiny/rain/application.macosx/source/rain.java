import processing.core.*; import java.applet.*; import java.awt.*; import java.awt.image.*; import java.awt.event.*; import java.io.*; import java.net.*; import java.text.*; import java.util.*; import java.util.zip.*; import javax.sound.midi.*; import javax.sound.midi.spi.*; import javax.sound.sampled.*; import javax.sound.sampled.spi.*; import java.util.regex.*; import javax.xml.parsers.*; import javax.xml.transform.*; import javax.xml.transform.dom.*; import javax.xml.transform.sax.*; import javax.xml.transform.stream.*; import org.xml.sax.*; import org.xml.sax.ext.*; import org.xml.sax.helpers.*; public class rain extends PApplet {int len = 150;
int[] x = new int[len];
int[] y = new int[len];
public void setup(){
  size(500,500,P3D);

for (int i=0;i<len;i++){
  x[i]=(int)random(0,width);
  y[i]=(int)random(0,height);
}
}
public void draw(){
  background(255);
  int newx = (int)random(0,width), newy = (int)random(0,height);

  for (int i=len-2;i>=0;i--){
    x[i+1]=x[i];
    y[i+1]=y[i];
  }
  x[0]=newx;
  y[0]=newy;
  
  noFill();
  for (int i=len-1;i>=0;i--){
    stroke(50 + i*3, 50 + i*3, 100 + i * 5);
    ellipse(x[i],y[i],i*3,i*3);
  }
}

  static public void main(String args[]) {     PApplet.main(new String[] { "rain" });  }}