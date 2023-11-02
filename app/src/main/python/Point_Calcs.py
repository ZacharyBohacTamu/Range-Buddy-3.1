import cv2
import math


# POINT CALCULATION
# CALCULATES POINTS BASED OFF OF WHICH TYPE OF TARGET USED
def Point_Counting(bullet_hole_coordinates, Type_of_Target, input_image):
    image = cv2.imread(input_image, cv2.IMREAD_GRAYSCALE)
    Cx = image.shape[1] // 2
    Cy = image.shape[0] // 2
    points = 0

    for bullet_hole_coordinate in bullet_hole_coordinates:
        Bx, By = bullet_hole_coordinate
        # calculates the radius of the bullet hole from the center of the target
        bullet_from_center_radius = math.sqrt((Cx - Bx) ** 2 + (Cy - By) ** 2)
        print(f"bullet radius: {bullet_from_center_radius}")
        Raidus_in = (bullet_from_center_radius / 80)  # 440 is the dpi for Pixel 3a
        print(f"bullet radius in in: {Raidus_in}")
        # looks for which target is being used and calculates points based off of that
        if Type_of_Target == "Pistol Target 25ft":  # may adjust once paper comes in
            if Raidus_in < .848:
                points += 15
                print("15")
            elif Raidus_in < 1.680:
                points += 10
                print("10")
            elif Raidus_in < 2.770:
                points += 9
                print("9")
            elif Raidus_in < 4.000:
                points += 8
                print("8")
            elif Raidus_in < 5.500:
                points += 7
                print("7")
            elif Raidus_in < 7.400:
                points += 6
                print("6")
            elif Raidus_in < 9.840:
                points += 5
                print("5")
            else:
                points += 0
        elif Type_of_Target == "Rifle Target 25yd":  # may adjust once paper comes in
            if Raidus_in < .848:
                points += 15
                print("15")
            elif Raidus_in < 1.680:
                points += 10
                print("10")
            elif Raidus_in < 2.770:
                points += 9
                print("9")
            elif Raidus_in < 4.000:
                points += 8
                print("8")
            elif Raidus_in < 5.500:
                points += 7
                print("7")
            elif Raidus_in < 7.400:
                points += 6
                print("6")
            elif Raidus_in < 9.840:
                points += 5
                print("5")
            else:
                points += 0

    return points
