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

public class spread extends PApplet {

public void setup(){
  size(1280,800,P3D);
  background(255);
  reset();
  frameRate(30);
}


/// controlled by a couple of parameters
/// might be fun to randomly vary them


float spawnprob = 0.3f;
int nheads=1;
float[] xheads={250},yheads={250},v={0.5f};
int[] dirs={2};
int[] cols;

public void reset(){
  nheads = 1 + PApplet.parseInt(random(3));
  xheads = new float[]{random(50,width-50),random(50,width-50),random(50,width-50)};
  yheads = new float[]{random(50,height-50),random(50,height-50),random(50,height-50)};
  v = new float[]{0.5f,0.5f,0.5f};
  dirs = new int[]{(int)random(0,4), (int)random(0,4), (int)random(0,4)};
  cols = new int[]{color(0),color(0),color(0)};
  oldch = 10;
  decaying = false;
}


//dirs: up, right, down, left
int[] xoff = {0,1,0,-1};
int[] yoff = {-1,0,1,0};

public int randir(int dir){
  boolean r = random(1) < 0.5f;
  if (dir == 0 || dir == 2) return r ? 1 : 3;
  else return r ? 0 : 2;
}

public int pp(float x, float y){
  int ix = (int)x, iy = (int)y;
  if (ix < 0 || ix >= width || iy < 0 || iy >= height) return -1;
  else return iy * width + ix;
}

float oldch = 10;

public void draw(){
  loadPixels();
  if (nheads == 0){
    decay();
  }else{
  for (int skip=0;skip<5;skip++){
  float[]
    nxheads = new float[nheads*2],
    nyheads = new float[nheads*2],
    nv = new float[nheads * 2];
  int[]
    ndirs = new int[nheads*2];
  int[]
    ncols = new int[nheads*2];
  int nh = 0;
  int white=color(255);
  float ch = 0;
  for (int i=0;i<nheads;i++){
    int oldp = pp(xheads[i], yheads[i]);
    xheads[i] += xoff[dirs[i]] * v[i];
    yheads[i] += yoff[dirs[i]] * v[i];

    int p = pp(xheads[i], yheads[i]);
    
    if (oldp == p){
      nxheads[nh]=xheads[i];
      nyheads[nh]=yheads[i];
      ndirs[nh]=dirs[i];
      nv[nh] = v[i];
      ncols[nh] = cols[i];
      nh++;
    }else if (p!=-1 && pixels[p] == white){
      pixels[p]=cols[i];
      ch++;
      if (random(1) < spawnprob){
        ndirs[nh] = randir(dirs[i]);
        nxheads[nh] = xheads[i];
        nyheads[nh] = yheads[i];
        ncols[nh] = cols[i];
        nv[nh] = constrain(v[i] * random(0.1f, 1.1f), 0.1f, 1.0f);
        nh++;
      }
      nxheads[nh]=xheads[i];
      nyheads[nh]=yheads[i];
      ndirs[nh]=dirs[i];
      float r = red(cols[i]), g=green(cols[i]),b =blue(cols[i]);
      r = constrain(r + random(-8,10), 0, 255);
      g = constrain(g + random(-8,10), 0, 255);
      b = constrain(b + random(-8,10), 0, 255);
      if (r + g + b > 500){
        int ix = (int)random(3);
        if (ix==0)r -= random(5,12);
        if (ix==1)g -= random(5,12);
        if (ix==2)b -= random(5,12);
      }
      ncols[nh]=color(r,g,b);
      nv[nh] = v[i];
      nh++;
    }
  }
  xheads = nxheads;
  yheads = nyheads;
  dirs = ndirs;
  v = nv;
  nheads = nh;
  cols = ncols;
  oldch = oldch * 0.9f + ch * 0.1f;
  if (oldch < 0.1f){
    nheads = 0;
  }
  }
  }
  updatePixels();
}

boolean decaying = false;
boolean[] vis;
int[] decx, decy;
int ndec;
int[] xs,ys;
public void floodfill(){
  int white = color(255);
  if (xs==null)xs = new int[vis.length+width*2+height*2+40];
  if (ys==null)ys = new int[vis.length+width*2+height*2+40];
  int npts = ndec;
  arraycopy(decx,0,xs,0,ndec);
  arraycopy(decy,0,ys,0,ndec);
  ndec = 0;
  while (npts != 0){
    int x = xs[npts-1], y = ys[npts-1];
    npts--;
    if (pixels[y * width + x] != white){
      decx[ndec] = x;
      decy[ndec] = y;
      ndec ++;
      pixels[y*width + x] = white;
      continue;
    }
    int p;
    if (x + 1 < width){
      p = y * width + x + 1;
      if (!vis[p]){
        vis[p] = true;
        xs[npts] = x + 1; ys[npts] = y; npts++;
      }
    }
    if (x - 1 >= 0){
      p = y * width + x - 1;
      if (!vis[p]){
        vis[p] = true;
        xs[npts] = x - 1; ys[npts] = y; npts++;
      }
    }
    if (y + 1 < height){
      p=(y+1)*width + x;
      if (!vis[p]){
        vis[p] = true;
        xs[npts] = x; ys[npts] = y+1; npts++;
      }
    }
    if (y-1 >= 0){
      p =(y-1)*width + x;
      if (!vis[p]){
        vis[p] = true;
        xs[npts] = x; ys[npts] = y-1; npts++;
      }
    }
  }
}
public void decay(){
  int white = color(255);
  if (!decaying){
    decx = new int[width*height];
    decx[0]=(int)random(50,width-50);
    decx[1]=(int)random(50,width-50);
    decy = new int[width*height];
    decy[0] = (int)random(50,height-50);
    decy[1] = (int)random(50,height-50);
    ndec = 1 + (int)random(2);
    decaying = true;
    vis = new boolean[width * height];
  }
  floodfill();
  if (ndec==0)reset();
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#DFDFDF", "spread" });
  }
}
