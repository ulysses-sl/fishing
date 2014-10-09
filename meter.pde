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
    ratio = 0.65;
    proximity = 0;
  }

  void reset()
  {
    meter = 0;
    proximity = 0;
  }

  void cooldown()
  {
    if (proximity > 0)
    {
      proximity -= 5;
    }
  }

  void heatup()
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

  void display(float x, float y)
  {
    noFill();
    strokeWeight(1);
    for (int i = 0; i < 10; i++)
    {
      stroke(255 - (proximity / 10) * (i + 1));
      arc(x + random(2), y + random(2), r1, ratio * r1, 0.1 + TWO_PI / 60 * (meter + i - 1), 0.1 + TWO_PI / 60 * (meter + i));
      arc(x + random(2), y + random(2), r2, ratio * r2, TWO_PI / 70 * (meter + i - 1), TWO_PI / 70 * (meter + i));
      arc(x + random(2), y + random(2), r3, ratio * r3, 0.3 + TWO_PI / 30 * (meter + i - 1), 0.3 + TWO_PI / 30 * (meter + i));
      arc(x + random(2), y + random(2), r4, ratio * r4, 0.5 + TWO_PI / 40 * (meter + i - 1), 0.5 +TWO_PI / 40 * (meter + i));
      arc(x + random(2), y + random(2), r5, ratio * r5, 0.6 + TWO_PI / 50 * (meter + i - 1), 0.6 + TWO_PI / 50 * (meter + i));
    }
    noStroke();
    strokeWeight(1);
    meter += TWO_PI / 50;
  }
}
