package com.dottycrumble;

import java.awt.Point;
import java.util.ArrayList;

public class PriorityQueue
{
  private Map map;
  private ArrayList<Point> queue;
  public PriorityQueue(Map map)
  {
    queue = new ArrayList<Point>();
    this.map = map;
  }
  public void add(Point coordinate)
  {
    Point current;
    if (size() > 0)
    {
      for (int i = 0; i < queue.size(); i++)
      {
        current = queue.get(i);
        if (map.getG(coordinate.x, coordinate.y) + map.getH(coordinate.x, coordinate.y) < map.getG(current.x, current.y) + map.getH(current.x, current.y))
        {
          queue.add(i, coordinate);
          return;
        }
      }
    }
    queue.add(coordinate);
  }
  public int size()
  {
    return queue.size();
  }
  public Point get(int index)
  {
    return queue.get(index);
  }
  public Point pull()
  {
    Point point = get(0);
    queue.remove(0);
    return point;
  }
  // Test
  // 10 1 5 4 2 7 8 6 9 3
  /*
  public static void main(String[] args)
  {
    Point coordinates[] = new Point[10];
    coordinates[0] = new Point(1, 0);
    coordinates[1] = new Point(2, 0);
    coordinates[2] = new Point(3, 0);
    coordinates[3] = new Point(4, 0);
    coordinates[4] = new Point(5, 0);
    coordinates[5] = new Point(6, 0);
    coordinates[6] = new Point(7, 0);
    coordinates[7] = new Point(8, 0);
    coordinates[8] = new Point(9, 0);
    coordinates[9] = new Point(10, 0);
    Map map = new Map(15, 15);
    map.setH(1, 0, 1);
    map.setH(2, 0, 5);
    map.setH(3, 0, 10);
    map.setH(4, 0, 4);
    map.setH(5, 0, 2);
    map.setH(6, 0, 8);
    map.setH(7, 0, 5);
    map.setH(8, 0, 5);
    map.setH(9, 0, 8);
    map.setH(10, 0, 0);
    PriorityQueue queue = new PriorityQueue(map);
    for (int i = 0; i < coordinates.length; i++)
      queue.add(coordinates[i]);
    System.out.println("List:");
    for (int i = 0; i < queue.size(); i++)
      System.out.printf("%d\t", queue.get(i).x);
    System.out.println();
  }
  */
}