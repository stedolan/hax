import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class squares extends PApplet {

int sz;
class Square{
  int x,y;
  int xoff, yoff;
  public void set(int a,int b,int xo,int yo){x=a;y=b;xoff=xo;yoff=yo;}
  public void draw(){
    int c = color(fore);
    int xp = x - sz/2, yp = y-sz/2;
    for (int i=0;i<=sz;i++){
      pixels[yp * width + xp + i] = c;
      pixels[(yp + sz) * width + xp + i] = c;
      pixels[(yp + i) * width + xp] = c;
      pixels[(yp + i) * width + xp + sz] = c;
    }
//    rect(x-sz/2, y-sz/2, sz, sz);
  }
  public void move(){
    x+=xoff;
    y+=yoff;
  }
}
Square[] sq = new Square[65536];
Square[] newsq = new Square[65536];
int nsq = 1;
public void setup(){
  size(512,512,P3D);
  frameRate(20);
  for (int i=0;i<sq.length;i++){
    sq[i] = new Square();
    newsq[i] = new Square();
  }
  reset();
  back=0;
  fore=30;
}
public void reset(){
  sq[0].set(256,256,0,0);
  nsq = 1;
  sz = 256;
  back += 30;
  fore += 30;
}
int expandtime = 128;
int expandframes = 10;
int back = 0;
int fore = 5;
public void draw(){
  background(back);
  if (expandframes != 0){
    for (int i=0;i<nsq;i++){
      sq[i].move();
    }
    expandframes--;
  }else{
    sz /= 2;
    int o = sz/2;
    if (nsq * 4 == sq.length){
      reset();
    }else{
      for (int i=0;i<nsq;i++){
        newsq[i*4].set(sq[i].x-o, sq[i].y-o, -1, -1);
        newsq[i*4+1].set(sq[i].x-o, sq[i].y+o, -1, 1);
        newsq[i*4+2].set(sq[i].x+o, sq[i].y-o, 1, -1);
        newsq[i*4+3].set(sq[i].x+o, sq[i].y+o, 1, 1);
      }
      Square[] tmp = sq;
      sq = newsq;
      newsq = tmp;
      expandframes = o;
      nsq *= 4;
    }
  }
  loadPixels();
  for (int i=0;i<nsq;i++)sq[i].draw();
  updatePixels();
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#DFDFDF", "squares" });
  }
}
