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

public class water extends PApplet {

int len = 150;
int[] x = new int[len];
int[] y = new int[len];
public void setup(){
  size(500,500,P3D);

for (int i=0;i<len;i++){
  x[i]=-200000;
  y[i]=-200000;
}
}
public void draw(){
  background(255);
  int newx = -200000, newy = -20000;
  if (mouseX != pmouseX || mouseY != pmouseY){
    newx=mouseX;
    newy=mouseY;
  }
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
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#DFDFDF", "water" });
  }
}
