## EffectiveBitmap

#### 通过JNI实现对图片文件的压缩，到达质量、大小、清晰度综合最优。

<img src="https://github.com/zengfw/EffectiveBitmap/blob/master/image/img1.jpg" width = "300" height = "374" align=center />

<img src="https://github.com/zengfw/EffectiveBitmap/blob/master/image/img2.jpg" width = "300" height = "308" align=center />

## 使用方法
将libs包中libeffective-bitmap.so、libjpegbither.so到项目中并加载即可。但是你必须使用类的限定名为“com.effective.bitmap.utils.EffectiveBitmapUtils”并添加：
```
public static native String compressBitmap(Bitmap bit, int w, int h, int quality, byte[] fileNameBytes,
                                               boolean optimize);
```
