from operator import abs
import range as range
from PIL import Image, ImageFilter


# compares the two images for shot location to be received
def comparison(input_image1, input_image2, output_image): # this needs output image
    # open images
    image1 = Image.open(input_image1).convert('L')
    image2 = Image.open(input_image2).convert('L')
    # blur images
    image1_blur = image1.filter(ImageFilter.GaussianBlur(radius=3)) # can change radius for
    image2_blur = image2.filter(ImageFilter.GaussianBlur(radius=3)) # different results

    # creates threshold higher results allows for better
    threshold = 70
    # 25ft .27 caliber thresh = 80 w/ 6 radius

    # creates blank image for later use
    output = Image.new("L", image1.size)
    # creates a List for different_pixels to be able to find average
    different_pixels = []  # needed for debugging purposes

    # double loop for x & y coordinates to compare each pixel
    for x in range(image1.width):
        for y in range(image1.height):
            # load pixels from each image
            pixel1 = image1_blur.getpixel((x, y))
            pixel2 = image2_blur.getpixel((x, y))

            # compare pixels
            if abs(pixel1 - pixel2) > threshold:
                different_pixels.append((x, y))  # appends different Pixel to list
                output.putpixel((x, y), 255)  # pixel is different, White it out
            else:
                output.putpixel((x, y), 0)  # pixel is same, Black it out

    output.save(output_image, "JPEG")

    return output
