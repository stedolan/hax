float ballrad = 50;
boolean repel = false;
void mouseReleased(){
//  repel=!repel;
  for(int i=0;i<parts.length;i++){
    float mdist = (float)Math.hypot(parts[i].x-mouseX,parts[i].y-mouseY);
    parts[i].vx += (parts[i].x-mouseX) * 500/(mdist*mdist);
    parts[i].vy += (parts[i].y-mouseY) * 500/(mdist*mdist);
  }  
}
float cx,cy;
class Particle{
  float x,y;
  float vx,vy;
  float ax,ay;
  float e = 0.5;
  int hue = (int)random(255);
  float sqd(float px, float py){
    return (x-px) * (x-px) + (y-py)*(y-py);
  }
  void force(float fact, float px, float py){
    float f = sqd(px,py);
    vx += (x-px) * fact/f;
    vy += (y-py) * fact/f;
  }
  void draw(float delta){
    if (positions[(int)x][(int)y] == this){
      positions[(int)x][(int)y]=null;
    }
    vx *= 0.9;
    vy *= 0.9;
    vx += ax*delta;
//   vy += ay*delta;
//    force(-delta * 150, 100, 250);
//    force(-delta * 150, 400, 250);
    force(-delta * 600, cx, cy);
    if (mousePressed){
      float mdist = (float)Math.hypot(x-mouseX, y-mouseY);
      vx -= (x-mouseX) * delta * 400/(mdist*mdist);
      vy -= (y-mouseY) * delta * 400/(mdist*mdist);
    }
    x+=vx;
    y+=vy;
    if (x < 5){x-=vx;vx=e*-vx;}
    if (x >= width-5){x-=vx;vx=e*-vx;}
    if (y < 5){y-=vy;vy=e*-vy;}
    if (y >= height-5){y-=vy;vy=e*-vy;}
    if (repel){
    float mdist = (float)Math.hypot(x-mouseX, y-mouseY);
    if (mdist < ballrad){
      vx += (x-mouseX) * delta * 400/(mdist*mdist);
      vy += (y-mouseY) * delta * 400/(mdist*mdist);
    }
    }
/*    if (Math.hypot(x-mouseX, y-mouseY) < ballrad){
      float 
    }*/
    
    float v = (float)Math.hypot(vx,vy);
//    stroke(255,255-v*50,255-v*100);
    if (hue>=256)hue=0;
    if (positions[(int)x][(int)y] !=null){
      //Pauli's exclusion principle:
      //If two particles have very similar positions
      //then they must have very different velocities
      Particle p = positions[(int)x][(int)y];
      float rx = random(-0.5,0.5), ry = random(-0.5,0.5);
      vx += rx;
      vy += ry;
      p.vx -= rx;
      p.vy -= ry;
    }
    positions[(int)x][(int)y]=this;
//    ellipse(x,y,10,10);
    pixels[(int)y*width + (int)x] = color(255,(int)(255-v*v*50*50),(int)(255-v*v*10000));
  }
  Particle(float _x, float _y, float _vx, float _vy, float _ax, float _ay){
    x=_x;y=_y;vx=_vx;vy=_vy;ax=_ax;ay=_ay;
  }
  Particle(float _x, float _y){
    this(_x,_y,random(-0.5,0.5),random(-0.5,0.5),0,3);
  }
}
Particle[] parts;
Particle[][] positions;
void setup(){
  size(1280,800,P3D);
  colorMode(RGB);
  parts = new Particle[10000];
  positions = new Particle[width][height];
  for (int i=0;i<parts.length;i++){
    if (random(1) < 0.5){
      parts[i] = new Particle(random(1)<0.5?5:width-5, random(height-5));
    }else{
      parts[i] = new Particle(random(width-5), random(1)<0.5?5:height-5);
    }
  }
  lastframe = millis();
}
long lastframe;
void draw(){
  long now = millis();
  float delta = 0.001 * (now - lastframe);
  cx = width/2 + width/2*cos(-0.001 * now);
  cy = height/2 + height/2*sin(-0.001 * now);
  background(0);
  loadPixels();
  for (int i=0;i<parts.length;i++)parts[i].draw(delta);
  updatePixels();
/*
  float xavg = 0,yavg=0;
  for (int i=0;i<parts.length;i++){
    xavg+=parts[i].x;
    yavg+=parts[i].y;
  }
  xavg/=parts.length;
  yavg/=parts.length;
  fill(50,255,50,100);
  ellipse(xavg,yavg,150,150);*/
  lastframe=now;
}
