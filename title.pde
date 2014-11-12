class Title {
  String titleText;
  boolean titleScreen;
  float textColor;
  
  Title() {
    //world.top.dettachImage();
    //world.bottom.dettachImage();
    world.remove(boat);
    titleText = "姜太公釣魚";
    textAlign(CENTER);
    textSize(100);
    titleScreen = true;
    textColor = 180;
  }
  
  void display() {
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
  
  boolean isTitle() {
    return titleScreen;
  }
  
  void textColorSet(float time) {
    textColor = 180 + 75 * (time / 2.0);
  }
  
  void textColorReset() {
    textColor = 180;
  }
  
  void titleOn() {
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
  
  void titleOff() {
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
