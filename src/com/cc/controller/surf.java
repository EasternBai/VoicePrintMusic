package com.cc.controller;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by niwei on 2017/4/28.
 */
public class surf 
{
    private static float nndrRatio = 0.7f;//这里设置既定值为0.7，该值可自行调整

    private static int matchesPointCount = 0;

    public float getNndrRatio() 
    {
        return nndrRatio;
    }

    public void setNndrRatio(float nndrRatio)
    {
        this.nndrRatio = nndrRatio;
    }

    public int getMatchesPointCount() 
    {
        return matchesPointCount;
    }

    public void setMatchesPointCount(int matchesPointCount) 
    {
        this.matchesPointCount = matchesPointCount;
    }

    @SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	public static boolean matchImage(Mat templateImage, Mat originalImage) 
    {
        MatOfKeyPoint templateKeyPoints = new MatOfKeyPoint();
        //指定特征点算法SURF
        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.ORB);
        //获取模板图的特征点
        featureDetector.detect(templateImage, templateKeyPoints);
        //提取模板图的特征点
        MatOfKeyPoint templateDescriptors = new MatOfKeyPoint();
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        System.out.println("提取模板图的特征点");
        descriptorExtractor.compute(templateImage, templateKeyPoints, templateDescriptors);

        //显示模板图的特征点图片
        Mat outputImage = new Mat(templateImage.rows(), templateImage.cols(), Imgcodecs.CV_LOAD_IMAGE_COLOR);
        System.out.println("在图片上显示提取的特征点");
        Features2d.drawKeypoints(templateImage, templateKeyPoints, outputImage, new Scalar(255, 0, 0), 0);
        Imgcodecs.imwrite("F:/music/specialPoint.jpg", outputImage);

        //获取原图的特征点
        MatOfKeyPoint originalKeyPoints = new MatOfKeyPoint();
        MatOfKeyPoint originalDescriptors = new MatOfKeyPoint();
        featureDetector.detect(originalImage, originalKeyPoints);
        System.out.println("提取原图的特征点");
        descriptorExtractor.compute(originalImage, originalKeyPoints, originalDescriptors);

        List<MatOfDMatch> matches = new LinkedList();
//        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_L1); 
        System.out.println("寻找最佳匹配");
        float result = 0;
        
//        if (!templateKeyPoints.size().empty() && !originalKeyPoints.size().empty()) 
//        {  
//        	DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_L1);  
//            MatOfDMatch matches = new MatOfDMatch();  
//            matcher.match(templateDescriptors, originalDescriptors, matches);  
//            // 最优匹配判断  
//            double minDist = 100;  
//            DMatch[] dMatchs = matches.toArray();  
//            int num = 0;  
//            for (int i = 0; i < dMatchs.length; i++) 
//            {  
//            	if (dMatchs[i].distance <= 2 * minDist)
//            	{  
////                     result += dMatchs[i].distance * dMatchs[i].distance;  
//                     num++;  
//                }  
//            }  
//            // 匹配度计算  
////            result /= num;
//            matchesPointCount = num;
//        }

        
        /**
         * knnMatch方法的作用就是在给定特征描述集合中寻找最佳匹配
         * 使用KNN-matching算法，令K=2，则每个match得到两个最接近的descriptor，然后计算最接近距离和次接近距离之间的比值，当比值大于既定值时，才作为最终match。
         */
        descriptorMatcher.knnMatch(templateDescriptors, originalDescriptors, matches, 2);

        System.out.println("计算匹配结果");
        final LinkedList<DMatch> goodMatchesList = new LinkedList();

        //对匹配结果进行筛选，依据distance进行筛选
        matches.forEach(new Consumer<MatOfDMatch>() 
        {
			@Override
			public void accept(MatOfDMatch match) 
			{
			    DMatch[] dmatcharray = match.toArray();
			    DMatch m1 = dmatcharray[0];
			    DMatch m2 = dmatcharray[1];

			    if (m1.distance <= m2.distance * nndrRatio) 
			    {
			        goodMatchesList.addLast(m1);
			    }
			}
		});
        
//        matches.forEach(match -> 
//        {
//            DMatch[] dmatcharray = match.toArray();
//            DMatch m1 = dmatcharray[0];
//            DMatch m2 = dmatcharray[1];
//
//            if (m1.distance <= m2.distance * nndrRatio) 
//            {
//                goodMatchesList.addLast(m1);
//            }
//        });

        matchesPointCount = goodMatchesList.size();

        //当匹配后的特征点大于等于 4 个，则认为模板图在原图中，该值可以自行调整
        if (matchesPointCount >= 30) 
        {
            System.out.println("模板图在原图匹配成功！");

//            final List<KeyPoint> templateKeyPointList = templateKeyPoints.toList();
//            final List<KeyPoint> originalKeyPointList = originalKeyPoints.toList();
//            final LinkedList<Point> objectPoints = new LinkedList();
//            final LinkedList<Point> scenePoints = new LinkedList();
//            goodMatchesList.forEach(new Consumer<DMatch>() {
//				@Override
//				public void accept(DMatch goodMatch) {
//				    objectPoints.addLast(templateKeyPointList.get(goodMatch.queryIdx).pt);
//				    scenePoints.addLast(originalKeyPointList.get(goodMatch.trainIdx).pt);
//				}
//			});
////            goodMatchesList.forEach(goodMatch -> 
////            {
////                objectPoints.addLast(templateKeyPointList.get(goodMatch.queryIdx).pt);
////                scenePoints.addLast(originalKeyPointList.get(goodMatch.trainIdx).pt);
////            });
//            MatOfPoint2f objMatOfPoint2f = new MatOfPoint2f();
//            objMatOfPoint2f.fromList(objectPoints);
//            MatOfPoint2f scnMatOfPoint2f = new MatOfPoint2f();
//            scnMatOfPoint2f.fromList(scenePoints);
//            //使用 findHomography 寻找匹配上的关键点的变换
//            Mat homography = Calib3d.findHomography(objMatOfPoint2f, scnMatOfPoint2f, Calib3d.RANSAC, 3);
//
//            /**
//             * 透视变换(Perspective Transformation)是将图片投影到一个新的视平面(Viewing Plane)，也称作投影映射(Projective Mapping)。
//             */
//            Mat templateCorners = new Mat(4, 1, CvType.CV_32FC2);
//            Mat templateTransformResult = new Mat(4, 1, CvType.CV_32FC2);
//            templateCorners.put(0, 0, new double[]{0, 0});
//            templateCorners.put(1, 0, new double[]{templateImage.cols(), 0});
//            templateCorners.put(2, 0, new double[]{templateImage.cols(), templateImage.rows()});
//            templateCorners.put(3, 0, new double[]{0, templateImage.rows()});
//            //使用 perspectiveTransform 将模板图进行透视变以矫正图象得到标准图片
//            Core.perspectiveTransform(templateCorners, templateTransformResult, homography);
//
//            //矩形四个顶点
//            double[] pointA = templateTransformResult.get(0, 0);
//            double[] pointB = templateTransformResult.get(1, 0);
//            double[] pointC = templateTransformResult.get(2, 0);
//            double[] pointD = templateTransformResult.get(3, 0);
//
//            //指定取得数组子集的范围
//            int rowStart = (int) pointA[1];
//            int rowEnd = (int) pointC[1];
//            int colStart = (int) pointD[0];
//            int colEnd = (int) pointB[0];
//            Mat subMat = originalImage.submat(rowStart, rowEnd, colStart, colEnd);
////            Highgui.imwrite("/Users/niwei/Desktop/opencv/原图中的匹配图.jpg", subMat);
//            Imgcodecs.imwrite("F:/music/matchOfOriginal.jpg", subMat);
//
//            //将匹配的图像用用四条线框出来
////            Core.line(originalImage, new Point(pointA), new Point(pointB), new Scalar(0, 255, 0), 4);//上 A->B
////            Core.line(originalImage, new Point(pointB), new Point(pointC), new Scalar(0, 255, 0), 4);//右 B->C
////            Core.line(originalImage, new Point(pointC), new Point(pointD), new Scalar(0, 255, 0), 4);//下 C->D
////            Core.line(originalImage, new Point(pointD), new Point(pointA), new Scalar(0, 255, 0), 4);//左 D->A
//            
//            Imgproc.line(originalImage, new Point(pointA), new Point(pointB), new Scalar(0, 255, 0), 4);//上 A->B
//            Imgproc.line(originalImage, new Point(pointB), new Point(pointC), new Scalar(0, 255, 0), 4);//右 B->C
//            Imgproc.line(originalImage, new Point(pointC), new Point(pointD), new Scalar(0, 255, 0), 4);//下 C->D
//            Imgproc.line(originalImage, new Point(pointD), new Point(pointA), new Scalar(0, 255, 0), 4);//左 D->A
//
//            MatOfDMatch goodMatches = new MatOfDMatch();
//            goodMatches.fromList(goodMatchesList);
//            Mat matchOutput = new Mat(originalImage.rows() * 2, originalImage.cols() * 2, Imgcodecs.CV_LOAD_IMAGE_COLOR);
//            Features2d.drawMatches(templateImage, templateKeyPoints, originalImage, originalKeyPoints, goodMatches, matchOutput, new Scalar(0, 255, 0), new Scalar(255, 0, 0), new MatOfByte(), 2);
//
////            Imgcodecs.imwrite("/Users/niwei/Desktop/opencv/特征点匹配过程.jpg", matchOutput);
////            Imgcodecs.imwrite("/Users/niwei/Desktop/opencv/模板图在原图中的位置.jpg", originalImage);
//            Imgcodecs.imwrite("F:/music/matching.jpg", matchOutput);
//            Imgcodecs.imwrite("F:/music/position.jpg", originalImage);
            
            return true;
        } 
        else 
        {
            System.out.println("模板图不在原图中！");
            return false;
        }

//        Imgcodecs.imwrite("F:/music/模板特征点.jpg", outputImage);
    }

//    public static void main(String[] args) 
//    {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//
//        String templateFilePath = "F:/music/timg1.jpg";
//        String originalFilePath = "F:/music/timg2.jpg";
//        //读取图片文件
//        Mat templateImage = Imgcodecs.imread(templateFilePath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
//        Mat originalImage = Imgcodecs.imread(originalFilePath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
//
//        surf imageRecognition = new surf();
//        surf.matchImage(templateImage, originalImage);
//
//        System.out.println("匹配的像素点总数：" + imageRecognition.getMatchesPointCount());
//    }
    
    
    
//    #include &lt;opencv2/core/core.hpp&gt;
//    #include &lt;opencv2/highgui/highgui.hpp&gt;
//    #include &lt;opencv2/nonfree/features2d.hpp&gt;
//    #include &lt;opencv2/legacy/legacy.hpp&gt;
//     
//    using namespace cv;
//    int main()
//    {
//        Mat image1=imread(&quot;../b1.png&quot;);
//        Mat image2=imread(&quot;../b2.png&quot;);
//        // 检测surf特征点
//        vector&lt;KeyPoint&gt; keypoints1,keypoints2;     
//        SurfFeatureDetector detector(400);
//        detector.detect(image1, keypoints1);
//        detector.detect(image2, keypoints2);
//        // 描述surf特征点
//        SurfDescriptorExtractor surfDesc;
//        Mat descriptros1,descriptros2;
//        surfDesc.compute(image1,keypoints1,descriptros1);
//        surfDesc.compute(image2,keypoints2,descriptros2);
//     
//        // 计算匹配点数
//        BruteForceMatcher&lt;L2&lt;float&gt;&gt;matcher;
//        vector&lt;DMatch&gt; matches;
//        matcher.match(descriptros1,descriptros2,matches);
//        std::nth_element(matches.begin(),matches.begin()+24,matches.end());
//        matches.erase(matches.begin()+25,matches.end());
//        // 画出匹配图
//        Mat imageMatches;
//        drawMatches(image1,keypoints1,image2,keypoints2,matches,
//            imageMatches,Scalar(255,0,0));
//     
//        namedWindow(&quot;image2&quot;);
//        imshow(&quot;image2&quot;,image2);
//        waitKey();
//     
//        return 0;
//    }
}

////特征点匹配，值越大匹配度越高  
//@Test  
//public void imgMatching2() throws Exception {  
//    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  
//    Mat src_base = Imgcodecs.imread("D:\\test\\test5.jpg");  
//    Mat src_test = Imgcodecs.imread("D:\\test\\test3.jpg");  
//    Mat gray_base = new Mat();  
//    Mat gray_test = new Mat();  
//    // 转换为灰度  
//    Imgproc.cvtColor(src_base, gray_base, Imgproc.COLOR_RGB2GRAY);  
//    Imgproc.cvtColor(src_test, gray_test, Imgproc.COLOR_RGB2GRAY);  
//    // 初始化ORB检测描述子  
//    FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.ORB);//特别提示下这里opencv暂时不支持SIFT、SURF检测方法，这个好像是opencv(windows) java版的一个bug,本人在这里被坑了好久。  
//    DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.ORB);  
//    // 关键点及特征描述矩阵声明  
//    MatOfKeyPoint keyPoint1 = new MatOfKeyPoint(), keyPoint2 = new MatOfKeyPoint();  
//    Mat descriptorMat1 = new Mat(), descriptorMat2 = new Mat();  
//    // 计算ORB特征关键点  
//    featureDetector.detect(gray_base, keyPoint1);  
//    featureDetector.detect(gray_test, keyPoint2);  
//    // 计算ORB特征描述矩阵  
//    descriptorExtractor.compute(gray_base, keyPoint1, descriptorMat1);  
//    descriptorExtractor.compute(gray_test, keyPoint2, descriptorMat2);  
//    float result = 0;  
//    // 特征点匹配  
//    System.out.println("test5：" + keyPoint1.size());  
//    System.out.println("test3：" + keyPoint2.size());  
//    if (!keyPoint1.size().empty() && !keyPoint2.size().empty()) {  
//        // FlannBasedMatcher matcher = new FlannBasedMatcher();  
//        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_L1);  
//        MatOfDMatch matches = new MatOfDMatch();  
//        matcher.match(descriptorMat1, descriptorMat2, matches);  
//        // 最优匹配判断  
//        double minDist = 100;  
//        DMatch[] dMatchs = matches.toArray();  
//        int num = 0;  
//        for (int i = 0; i < dMatchs.length; i++) {  
//            if (dMatchs[i].distance <= 2 * minDist) {  
//                result += dMatchs[i].distance * dMatchs[i].distance;  
//                num++;  
//            }  
//        }  
//        // 匹配度计算  
//        result /= num;  
//    }  
//    System.out.println(result);  
//}  
