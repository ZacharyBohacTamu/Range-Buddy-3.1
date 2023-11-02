import cv2
import math


# BULLET_COORDS
# FINDS CENTER EACH BULLET HOLE AND PUTS THEM INTO LIST
def bullet_coords(input_image):
    # Reads image
    img = cv2.imread(input_image, 0)

    # Sets up a threshold to separate wht & blk pixels
    _, Threshold = cv2.threshold(img, 200, 255, cv2.THRESH_BINARY)

    # Find contours in the threshold image
    Bullet_Hole, _ = cv2.findContours(Threshold, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    # Creates a list to hold bullet hole coordinates
    bullet_hole_coordinates = []

    # Loop through the contours to find the coordinates
    for contour in Bullet_Hole:
        # Finds the center of each bullet hole
        Center = cv2.moments(contour)
        if Center["m00"] != 0:  # checks if contour (bullet hole) greater than 0
            cX = int(Center["m10"] / Center["m00"])  # averages all X coordinates
            cY = int(Center["m01"] / Center["m00"])  # averages all y coordinates
            bullet_hole_coordinates.append((cX, cY))  # adds X & Y coordinates to list

    return bullet_hole_coordinates
