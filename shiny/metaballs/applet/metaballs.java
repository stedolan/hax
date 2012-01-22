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

public class metaballs extends PApplet {

public void setup(){
  size(500,500,P3D);
  noLoop();
}
public float f(float x,float y){return 1/(x*x + y*y);}
public void draw(){
  mx=mouseX;my=mouseY;
  background(0xffFFFF00);
  loadPixels();
  int p = 0;
  for (int y=0;y<height;y++){
    for (int x=0;x<width;x++){
      float f1 = f(x-250,y-250), f2 = f(x-mx,y-my);
      float t = f1+f2;
      if (t > 0.0003f)
        pixels[p] = color(255 * f1/t);
      p++;
    }
  }
  updatePixels();
}
int mx,my;
public void mouseMoved(){redraw();}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#DFDFDF", "metaballs" });
  }
}
