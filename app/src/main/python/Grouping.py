from PIL import Image, ImageFilter, ImageChops
import io
import cv2
import math


def Grouping(coordinates):
    # function to hold the diameter of the group
    Group_dist = 0
    DPI = 100  # 440 for Pixel 3A

    for i in range(len(coordinates)):
        for j in range(i + 1, len(coordinates)):
            x1, y1 = coordinates[i]
            x2, y2 = coordinates[j]

            distance = math.sqrt((x1 - x2) ** 2 + (y1 - y2) ** 2)
            if distance > Group_dist:
                Group_dist = distance

    Group_dist = Group_dist / DPI
    # variables needed to average
    sumX = 0  # sum of X
    sumY = 0  # sum of Y
    # loops through to add up the coordinates
    for x, y in coordinates:
        sumX += x
        sumY += y
    # creates average for both coorindates
    avgX = sumX / len(coordinates)
    avgY = sumY / len(coordinates)

    print(f"Middle of Shot is ({avgX},{avgY})") # really only need Group_dist
    print(f"Grouping Size is {Group_dist} inches")

    return Group_dist, avgX, avgY
