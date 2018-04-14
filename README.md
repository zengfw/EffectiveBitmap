## EffectiveBitmap

#### 通过JNI实现对图片文件的压缩，到达质量、大小、清晰度综合最优。

<img src="https://github.com/zengfw/EffectiveBitmap/blob/master/image/img1.jpg" width = "300" height = "374" align=center />

<img src="https://github.com/zengfw/EffectiveBitmap/blob/master/image/img2.jpg" width = "300" height = "308" align=center />

#### jni_278KB
<img src="https://github.com/zengfw/EffectiveBitmap/blob/master/image/jni_278KB.png" width = "270" height = "270" align=center />

#### quality_484KB
<img src="https://github.com/zengfw/EffectiveBitmap/blob/master/image/quality_484KB.png" width = "270" height = "270" align=center />

#### sample_199KB
<img src="https://github.com/zengfw/EffectiveBitmap/blob/master/image/sample_199KB.png" width = "270" height = "270" align=center />

#### size_238KB
<img src="https://github.com/zengfw/EffectiveBitmap/blob/master/image/size_238KB.png" width = "270" height = "270" align=center />

 - 采样率、尺寸压缩不是我们要的结果
 - JNI、质量压缩设置的压缩质量值均为30，JNI是278KB,直接质量压缩是484KB，综合起来，JNI最优

## 使用方法
1.将libs包中libeffective-bitmap.so、libjpegbither.so到项目中并加载即可。但是你必须使用类的限定名为“com.effective.bitmap.utils.EffectiveBitmapUtils”并添加：
```
public static native String compressBitmap(Bitmap bit, int w, int h, int quality, byte[] fileNameBytes,
                                               boolean optimize);
```
2.download项目，构建修改自己想要的abi，以及使用类的限定名。
