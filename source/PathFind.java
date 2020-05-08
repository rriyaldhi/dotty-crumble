package com.dottycrumble;

import java.awt.Point;
import java.util.ArrayList;

public class PathFind
{

  public static double heuristic(Point current, Point destination)
  {
    return diagonalDistance(current, destination);
  }
  public static double diagonalDistance(Point current, Point destination)
  {
    int D = 1;
    double D2 = Math.sqrt(2);
    double dx = Math.abs(current.x - destination.x);
    double dy = Math.abs(current.y - destination.y);
    return D * (dx + dy) + (D2 - 2 * D) * Math.min(dx, dy); 
  }
  public static double manhattanDistance(Point current, Point destination)
  {
    int D = 1;
    double dx = Math.abs(current.x - destination.x);
    double dy = Math.abs(current.y - destination.y);
    return D * (dx + dy); 
  }

  public static void check(Map map, PriorityQueue queue, Point current, Point source, Point destination, int x, int y)
  {
    double h = heuristic(new Point(x, y), destination);
    if (map.getState(x, y) >= 0)
    {
      if (map.getState(x, y) == Map.OPENED)
      {
        if (h + map.getG(current.x, current.y) + 1 < map.getG(x, y) + map.getH(x, y))
        {
          map.setG(x, y, map.getG(current.x, current.y) + 1);
          map.setH(x, y, h);
          map.setParent(x, y, current);
        }
      }
      else
      {
        map.setState(x, y, Map.OPENED);
        map.setG(x, y, map.getG(current.x, current.y)  + 1);
        map.setH(x, y, h);
        map.setParent(x, y, current);
        queue.add(new Point(x, y));
      }
    }
  }

  public static void aStar(Map map, Point source, Point destination, ArrayList<Point> path)
  {
    PriorityQueue queue = new PriorityQueue(map);
    Point current;
    Point neighbor;
    int x;
    int y;
    map.setState(source.x, source.y, Map.SOURCE);
    map.setState(destination.x, destination.y, Map.DESTINATION);
    queue.add(source);
    
    while (true)
    {
      current = queue.pull();
      map.setState(current.x, current.y, Map.EXPLORED);
      x = current.x + 1;
      y = current.y;
      check(map, queue, current, source, destination, x, y);
      x = current.x + 1;
      y = current.y + 1;
      check(map, queue, current, source, destination, x, y);
      x = current.x;
      y = current.y + 1;
      check(map, queue, current, source, destination, x, y);
      x = current.x - 1;
      y = current.y + 1;
      check(map, queue, current, source, destination, x, y);
      x = current.x - 1;
      y = current.y;
      check(map, queue, current, source, destination, x, y);
      x = current.x - 1;
      y = current.y - 1;
      check(map, queue, current, source, destination, x, y);
      x = current.x;
      y = current.y - 1;
      check(map, queue, current, source, destination, x, y);
      x = current.x + 1;
      y = current.y - 1;
      check(map, queue, current, source, destination, x, y);

      if (queue.size() == 0)
      {
        map.clean();
        return;
      }

      if (current.equals(destination))
      {
        do
        {
          map.setState(current.x, current.y, Map.PATH);
          path.add(new Point(current.x, current.y));
          current = map.getParent(current.x, current.y);
        }
        while (current != null);
        map.clean();
        return;
      }

    }
  }
}