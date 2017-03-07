package com.catira.opencvdemo.test.services;


/**
 * Created by Timo on 01.11.2016.
 */
/*
@RunWith(AndroidJUnit4.class)
public class BikeImageIdentifierTest {


    private CountDownLatch lock = new CountDownLatch(1);

    @Test
    public void testFrontalBikeImage() throws IOException, InterruptedException {
        Context context = InstrumentationRegistry.getContext();
        Context targetContext = InstrumentationRegistry.getTargetContext();

        if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, targetContext, new BaseLoaderCallback(context) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS:
                    {
                        Log.i("TEST", "OpenCV loaded successfully");
                    } break;
                    default:
                    {
                        super.onManagerConnected(status);
                    } break;
                }

                lock.countDown();
            }
        }))
        {
            Log.e("TEST", "Cannot connect to OpenCV Manager");
        }

        lock.await(2000, TimeUnit.MILLISECONDS);

        InputStream is = context.getAssets().open("bike1.jpg");
        Bitmap testImg = BitmapFactory.decodeStream(is);
        Mat testMat = new Mat(testImg.getHeight(), testImg.getWidth(), CvType.CV_8UC1);
        Utils.bitmapToMat(testImg, testMat);

        BikeImageIdentifier bii = new BikeImageIdentifier(targetContext);
        bii.loadBikeDimensions(testMat);

        Assert.assertTrue(bii.getLastMatchedDimensions().size() == 1);
    }
}
*/