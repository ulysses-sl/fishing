import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import fisica.*; 
import de.voidplus.leapmotion.*; 
import gifAnimation.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class lmart extends PApplet {





LeapMotion leap;

FWorld world;
FBox boat;
FBox stone;

ArrayList<FBox> goodFloaters;
ArrayList<FBox> badFloaters;

ArrayList<TextEffect> effects;

FishingMeter meter;
FishCounter fcounter;
Gif boatImg;
PImage mountImg;
PImage stoneImg;

Title title;
PFont font;

float whirlX;
float whirlY;

int timer;

public void setup()
{
  size(displayWidth, 700, P2D);
  frameRate(24);
  background(255);
  fill(255);
  noStroke();

  whirlX = random(200, width-200);
  whirlY = random(300, height-300);
  
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
  
  stone = new FBox(width / 4.7f, height * 3.0f / 7);
  stone.setPosition(width * 9.0f / 10, height - height * 3.0f / 14);
  stone.setStatic(true);
  stone.setStroke(0, 0);
  stone.setFill(0, 0);
  world.add(stone);

  meter = new FishingMeter();
  fcounter = new FishCounter();
  
  goodFloaters = new ArrayList<FBox>();
  badFloaters = new ArrayList<FBox>();
  effects = new ArrayList<TextEffect>();
  for (int i = 0; i < 3; i++) {
    FBox goodFloater = new FBox(100 + random(50), 50 + random(20));
    goodFloater.setStroke(128, 255, 128);
    goodFloater.setRotatable(false);
    goodFloater.setPosition(width / 6 + random(width * 2 / 3), height / 4 + random(height / 2));
    goodFloaters.add(goodFloater);
    
    FBox badFloater = new FBox(100 + random(50), 50 + random(20));
    badFloater.setStroke(255, 128, 128);
    badFloater.setRotatable(false);
    badFloater.setPosition(width / 6 + random(width * 2 / 3), height / 4 + random(height / 2));
    badFloaters.add(badFloater);
  }
  
  font = createFont("WenQuanYi Micro Hei", 100);
  textFont(font);  
  
  title = new Title();
  timer = 0;
}

public void draw(){
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
                title.textColorSet((timer * 1.0f) / 24);
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
        // whirl
        stroke(128, 128, 255, 50);
        fill(128, 128, 200, 50);
        ellipse(whirlX, whirlY, 100, 50);
        // collision test
        for (FBox gfloater: goodFloaters) {
            if (boat.isTouchingBody(gfloater) && effects.size() < 3) {
                effects.add(new TextEffect("Good!", gfloater.getX(), gfloater.getY()));
            }
        }
        for (FBox bfloater: badFloaters) {
            if (boat.isTouchingBody(bfloater) && effects.size() < 3) {
                effects.add(new TextEffect("Bad!", bfloater.getX(), bfloater.getY()));
            }
        }

        // effect text
        if (effects.size() > 0) {
            while (effects.size() > 0 && effects.get(0).dead()) {
                effects.remove(0);
            }
            for (TextEffect te : effects) {
                te.display();
            }
        }
      // ...
      int fps = leap.getFrameRate();
  
  
      // ========= HANDS =========
  
      for(Hand hand : leap.getHands()){
  
          Finger  finger_index     = hand.getIndexFinger();
  
          PVector finger_position   = finger_index.getPosition();
  
          int     touch_zone        = finger_index.getTouchZone();
          float   touch_distance    = finger_index.getTouchDistance();
  
          float realX = 1.8f * finger_position.x - width / 2.1f;
          float realY = 4 * finger_position.y - height * 2.5f;
  
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
                  if (abs(boat.getX() - whirlX) < 100 && abs(boat.getY() - whirlY) < 50 && abs(boat.getX() - realX) < 20 && abs(boat.getY() - realY) < 20)
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

public void leapOnInit(){
    // println("Leap Motion Init");
}
public void leapOnConnect(){
    // println("Leap Motion Connect");
}
public void leapOnFrame(){
    // println("Leap Motion Frame");
}
public void leapOnDisconnect(){
    // println("Leap Motion Disconnect");
}
public void leapOnExit(){
    // println("Leap Motion Exit");
}
class FishCounter {
  int fish;
  float xpos, ypos;
  
  FishCounter() {
    fish = 0;
    ypos = height / 2;
    xpos = 30;
  }
  
  public void display() {
    textAlign(CENTER);
    textSize(20);
    fill(0);
    text(fish, xpos, ypos);
  }
  
  public void add() {
    fish++;
  }
  
  public void reset() {
    fish = 0;
  }
}
class FishingMeter
{
  float meter, r1, r2, r3, r4, r5, ratio, proximity;
  FishingMeter()
  {
    meter = 0;
    r1 = 50;
    r2 = 42;
    r3 = 31;
    r4 = 22;
    r5 = 13;
    ratio = 0.65f;
    proximity = 0;
  }

  public void reset()
  {
    meter = 0;
    proximity = 0;
  }

  public void cooldown()
  {
    if (proximity > 0)
    {
      proximity -= 5;
    }
  }

  public void heatup()
  {
    if (proximity < 255)
    {
      proximity += 1;
    }
    else {
      fcounter.add();
      reset();
    }
  }

  public void display(float x, float y)
  {
    noFill();
    strokeWeight(1);
    for (int i = 0; i < 10; i++)
    {
      stroke(255 - (proximity / 10) * (i + 1));
      arc(x + random(2), y + random(2), r1, ratio * r1, 0.1f + TWO_PI / 60 * (meter + i - 1), 0.1f + TWO_PI / 60 * (meter + i));
      arc(x + random(2), y + random(2), r2, ratio * r2, TWO_PI / 70 * (meter + i - 1), TWO_PI / 70 * (meter + i));
      arc(x + random(2), y + random(2), r3, ratio * r3, 0.3f + TWO_PI / 30 * (meter + i - 1), 0.3f + TWO_PI / 30 * (meter + i));
      arc(x + random(2), y + random(2), r4, ratio * r4, 0.5f + TWO_PI / 40 * (meter + i - 1), 0.5f +TWO_PI / 40 * (meter + i));
      arc(x + random(2), y + random(2), r5, ratio * r5, 0.6f + TWO_PI / 50 * (meter + i - 1), 0.6f + TWO_PI / 50 * (meter + i));
    }
    noStroke();
    strokeWeight(1);
    meter += TWO_PI / 50;
  }
}
class TextEffect {
    String content;
    int size;
    float x, y;
    int life;

    TextEffect(String sometext, float xx, float yy) {
        content = sometext;
        size = 20;
        x = xx;
        y = yy;
        life = 50;
    }

    public void decLife() {
        life--;
    }

    public void decPos() {
        y--;
    }

    public boolean dead() {
        return life <= 0;
    }

    public void display() {
        textSize(size);
        textAlign(CENTER);
        fill(255 - 255 / 50 * life);
        text(content, x, y);
        decPos();
        decLife();
    }
}
class Title {
  String titleText;
  boolean titleScreen;
  float textColor;
  
  Title() {
    //world.top.dettachImage();
    //world.bottom.dettachImage();
    world.remove(boat);
    titleText = "\u59dc\u592a\u516c\u91e3\u9b5a";
    textAlign(CENTER);
    textSize(100);
    titleScreen = true;
    textColor = 180;
  }
  
  public void display() {
      float offset_h = 0;
      float offset_v = 0;
      float darkness = 0;
      if (random(100) > 90) {
        offset_h = -2 + random(4);
        offset_v = -2 + random(4);
        darkness = -30 + random(60);
      }
      textAlign(CENTER);
      textSize(100);
      fill(textColor + darkness);
      text(titleText, width / 2 + offset_h, height / 2 + offset_v);
  }
  
  public boolean isTitle() {
    return titleScreen;
  }
  
  public void textColorSet(float time) {
    textColor = 180 + 75 * (time / 2.0f);
  }
  
  public void textColorReset() {
    textColor = 180;
  }
  
  public void titleOn() {
    world.remove(boat);
    for (FBox floater : goodFloaters) {
        world.remove(floater);
    }
    for (FBox floater : badFloaters) {
        world.remove(floater);
    }
    fcounter.reset();
    titleScreen = true;
    textColor = 180;
  }
  
  public void titleOff() {
    for (FBox floater : goodFloaters) {
        world.add(floater);
    }
    for (FBox floater : badFloaters) {
        world.add(floater);
    }
    titleScreen = false;
    boat.setVelocity(0, 0);
    boat.resetForces();
    world.add(boat);
    boat.setPosition(width / 2, height / 2);
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "lmart" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
