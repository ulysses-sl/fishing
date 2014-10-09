import fisica.*;
import de.voidplus.leapmotion.*;
import gifAnimation.*;

LeapMotion leap;

FWorld world;
FBox boat;
FBox stone;

ArrayList<FBox> floaters;

FishingMeter meter;
FishCounter fcounter;
Gif boatImg;
PImage mountImg;
PImage stoneImg;

Title title;
PFont font;

int timer;

void setup()
{
  size(displayWidth, 700, P2D);
  frameRate(24);
  background(255);
  fill(255);
  noStroke();
  
  leap = new LeapMotion(this);

  Fisica.init(this);
  world = new FWorld();

  world.setEdges();
  world.left.setHeight(height * 3 / 4);
  world.right.setHeight(height * 3 / 4);
  world.top.setPosition(width / 2 - 100, height / 8 - 1);
  world.bottom.setPosition(width / 2, height - height / 4 + 1);
  
  world.left.setFill(255);
  world.right.setFill(255);
  world.top.setFill(255);
  world.bottom.setFill(255);
  world.left.setStroke(255);
  world.right.setStroke(255);
  world.top.setStroke(255);
  world.bottom.setStroke(255);

  mountImg = loadImage("mount.gif");
  stoneImg = loadImage("stone.gif");
  mountImg.resize(width * 8 / 5, 0);
  stoneImg.resize(width * 2, 0);
  world.top.attachImage(mountImg);
  world.bottom.attachImage(stoneImg);
  
  world.setGravity(0, 0);

  boat = new FBox(200, 80);
  boat.setPosition(width/2, height/2);
  boat.setRotatable(false);
  world.add(boat);
  //boatImg = loadImage("fishing.gif");
  boatImg = new Gif(this, "fishing.gif");
  boatImg.play();
  boat.attachImage(boatImg);
  
  stone = new FBox(width / 4.7, height * 3.0 / 7);
  stone.setPosition(width * 9.0 / 10, height - height * 3.0 / 14);
  stone.setStatic(true);
  stone.setStroke(0, 0);
  stone.setFill(0, 0);
  world.add(stone);

  meter = new FishingMeter();
  fcounter = new FishCounter();
  
  floaters = new ArrayList<FBox>();
  for (int i = 0; i < 3; i++) {
    FBox floater = new FBox(100 + random(50), 50 + random(20));
    floater.setRotatable(false);
    floater.setPosition(width / 6 + random(width * 2 / 3), height / 4 + random(height / 2));
    floaters.add(floater);
    world.add(floater);
  }
  
  font = createFont("WenQuanYi Micro Hei", 100);
  textFont(font);  
  
  title = new Title();
  timer = 0;
}

void draw(){
  background(255);
  world.step();
  world.draw();
  if (title.isTitle()) {
    title.display();
    
    for(Hand hand : leap.getHands()){

        Finger  finger_index     = hand.getIndexFinger();

        PVector finger_position   = finger_index.getPosition();

        int     touch_zone        = finger_index.getTouchZone();
        float   touch_distance    = finger_index.getTouchDistance();

        switch(touch_zone){
            case -1: // None
                timer = 0;
                title.textColorReset();
            case 0: // Hovering
            case 1: // Touching
                println(timer);
                timer++;
                title.textColorSet((timer * 1.0) / 24);
                if (timer > 40) {
                  title.titleOff();
                  timer = 0;
                }
                break;
        }
    }
  }
  else {
    fcounter.display();
    timer++;
    if (timer >= 24 * 10 ) {
      timer = 0;
      title.titleOn();
    }
    else {
      // ...
      int fps = leap.getFrameRate();
  
  
      // ========= HANDS =========
  
      for(Hand hand : leap.getHands()){
  
          Finger  finger_index     = hand.getIndexFinger();
  
          PVector finger_position   = finger_index.getPosition();
  
          int     touch_zone        = finger_index.getTouchZone();
          float   touch_distance    = finger_index.getTouchDistance();
  
          float realX = 1.8 * finger_position.x - width / 2.1;
          float realY = 4 * finger_position.y - height * 2.5;
  
          switch(touch_zone){
              case -1: // None
                  break;
              case 0: // Hovering
              case 1: // Touching
                  timer = 0;
                  if (hand.isLeft())
                    fill(255, 0, 0, 100);
                  else
                    fill(0, 0, 255, 100);
                  ellipse(realX, realY, 10, 10);
                  if (abs(boat.getX() - realX) < 20 && abs(boat.getY() - realY) < 20)
                  {
                    meter.heatup();
                  }
                  else if (abs(boat.getX() - realX) < 100 && abs(boat.getY() - realY) < 100)
                  {
                    meter.cooldown();
                    boat.addForce(10 * (realX - boat.getX()), 10 * (realY - boat.getY()));
                  }
                  else
                  {
                    meter.reset();
                  }
                  meter.display(boat.getX() + 90, boat.getY() + 20);
                  break;
          }
      }
  
    }
  
  }

}


// ========= CALLBACKS =========

void leapOnInit(){
    // println("Leap Motion Init");
}
void leapOnConnect(){
    // println("Leap Motion Connect");
}
void leapOnFrame(){
    // println("Leap Motion Frame");
}
void leapOnDisconnect(){
    // println("Leap Motion Disconnect");
}
void leapOnExit(){
    // println("Leap Motion Exit");
}
