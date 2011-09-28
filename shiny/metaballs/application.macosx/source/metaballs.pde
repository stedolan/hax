void setup(){
  size(500,500,P3D);
  noLoop();
}
float f(float x,float y){return 1/(x*x + y*y);}
void draw(){
  mx=mouseX;my=mouseY;
  background(#FFFF00);
  loadPixels();
  int p = 0;
  for (int y=0;y<height;y++){
    for (int x=0;x<width;x++){
      float f1 = f(x-250,y-250), f2 = f(x-mx,y-my);
      float t = f1+f2;
      if (t > 0.0003)
        pixels[p] = color(255 * f1/t);
      p++;
    }
  }
  updatePixels();
}
int mx,my;
void mouseMoved(){redraw();}
