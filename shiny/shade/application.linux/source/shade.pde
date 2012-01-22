void setup(){
  size(500,500);
  frameRate(30);
}


float[] triangle_x = {40, 170, 190, 100, 200, 125, 400, 480, 350};
float[] triangle_y = {10, 180, 120, 480, 290, 350, 200, 270, 310};

float[] edge_x = new float[6];
float[] edge_y = new float[6];

void drawTriangles(){
  stroke(0);
//  fill(0,0,255);
  fill(255);
  int i = 0;
  while (i < triangle_x.length){
    float ax = triangle_x[i], bx = triangle_x[i+1], cx = triangle_x[i+2];
    float ay = triangle_y[i], by = triangle_y[i+1], cy = triangle_y[i+2];
    beginShape();
    vertex(ax,ay);
    vertex(bx,by);
    vertex(cx,cy);
    endShape(CLOSE);
    i += 3;
  }
}

void drawEdges(){
  color[] clrs = {color(255,0,0), color(0,255,0)};
  for (int i=0;i<edge_x.length;i++){
    fill(clrs[i%2]);
    ellipse(edge_x[i], edge_y[i], 10, 10);
  }
}
void drawShadows(float x, float y){
  noStroke();
  fill(0);
  int i = 0;
  while (i<edge_x.length){
    float ex = x + (edge_x[i] - x) * 1000, ey = y + (edge_y[i] - y) * 1000;
    float ex2 = x + (edge_x[i+1] - x) * 1000, ey2 = y + (edge_y[i+1] - y) * 1000;
    beginShape();
    vertex(edge_x[i], edge_y[i]);
    vertex(ex,ey);
    vertex(ex2, ey2);
    vertex(edge_x[i+1], edge_y[i+1]);
    endShape(CLOSE);
    i+=2;
  }
}

boolean ang_lt(float x1, float x2, float y1, float y2){
 return x1*y2 < y1*x2;
}

void calcEdges(float x, float y){
  int i = 0, j = 0;
  while (i < triangle_x.length){
    float ax = triangle_x[i], bx = triangle_x[i+1], cx = triangle_x[i+2];
    float ay = triangle_y[i], by = triangle_y[i+1], cy = triangle_y[i+2];
 
    float dax = ax - x, day = ay - y, dbx = bx - x, dby = by - y, dcx = cx - x, dcy = cy - y;
    float min_x, min_y, max_x, max_y;
    
    min_x = dax; min_y = day; max_x = dax; max_y = day;
    if (!ang_lt(min_x, dbx, min_y, dby)){
      min_x = dbx;
      min_y = dby;
    }
    if (!ang_lt(min_x, dcx, min_y, dcy)){
      min_x = dcx;
      min_y = dcy;
    }
    
    
    if (!ang_lt(dbx, max_x, dby, max_y)){
      max_x = dbx;
      max_y = dby;
    }
    if (!ang_lt(dcx, max_x, dcy, max_y)){
      max_x = dcx;
      max_y = dcy;
    }
    
    
    
    edge_x[j] = x+min_x;
    edge_y[j] = y+min_y;
    edge_x[j+1] = x+max_x;
    edge_y[j+1] = y+max_y;
    i += 3;
    j += 2;
  }
}

void draw(){
  background(255);
  calcEdges(mouseX, mouseY);
//  drawEdges();
  drawShadows(mouseX, mouseY);
  drawTriangles();
  loadPixels();
  int p = 0;
  color white = color(255);
  for (int y=0;y<height;y++){
    for (int x=0;x<width;x++){
      if (pixels[p] == white){
        float d = dist(x,y,mouseX,mouseY);
        pixels[p] = color(255 - constrain(d > 500 ? 255 : 0, 0, 255));
      }
      p++;
    }
  }
  updatePixels();
}
