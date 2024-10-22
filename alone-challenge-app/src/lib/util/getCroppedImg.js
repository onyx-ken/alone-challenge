export const getCroppedImg = async (imageSrc, croppedAreaPixels, zoom = 1) => {
    const createImage = (url) =>
        new Promise((resolve, reject) => {
            const image = new Image();
            image.onload = () => {
                console.log('Image loaded successfully:', image);
                resolve(image);
            };
            image.onerror = (error) => {
                console.error('Error loading image:', error);
                reject(error);
            };
            image.src = url;
        });

    const image = await createImage(imageSrc);

    const canvas = document.createElement('canvas');
    const ctx = canvas.getContext('2d');

    // Set canvas dimensions
    canvas.width = croppedAreaPixels.width;
    canvas.height = croppedAreaPixels.height;

    console.log('Drawing image with the following parameters:');
    console.log('croppedAreaPixels:', croppedAreaPixels);
    console.log('Canvas Width:', canvas.width, 'Canvas Height:', canvas.height);

    // Draw the image onto the canvas
    ctx.drawImage(
        image,
        croppedAreaPixels.x,
        croppedAreaPixels.y,
        croppedAreaPixels.width,
        croppedAreaPixels.height,
        0,
        0,
        canvas.width,
        canvas.height
    );

    // Return the canvas as a blob
    return new Promise((resolve, reject) => {
        canvas.toBlob((blob) => {
            if (blob) {
                resolve(blob);
            } else {
                reject(new Error('Canvas is empty'));
            }
        }, 'image/jpeg');
    });
};
