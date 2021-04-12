package com.hp.utils_store.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import java.io.*

/**

 * Author：admin_h on 2021/4/8 15:50

 */
object ImageUtil {

    /**
     * 保存bitmap到文件
     */
    fun saveBitmap(bitmap: Bitmap?, path: String?): Boolean {
        return if (bitmap == null || TextUtils.isEmpty(path)) {
            false
        } else try {
            val f = File(path)
            if (!f.exists()) {
                f.parentFile.mkdirs()
                f.createNewFile()
            }
            val fos = FileOutputStream(f)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            true
        } catch (e: FileNotFoundException) {
            false
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
        }

    }

    /**
     * 保存图片到系统相册
     * @param bmp 获取的bitmap数据
     * @param picName 自定义的图片名
     */
    fun saveBmp2Gallery(context: Context, bmp: Bitmap, picName: String) : Boolean{
        var fileName: String? = null
        //系统相册目录
        val galleryPath = (Environment.getExternalStorageDirectory()
            .toString() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator)

        // 声明文件对象
        var file: File? = null
        try {
            // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
            file = File(galleryPath, "$picName.jpg")

            if(saveBitmap(bmp, file.path)){
                //通知相册更新
                MediaStore.Images.Media.insertImage(
                    context.contentResolver,
                    bmp, fileName, null
                )
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val uri = Uri.fromFile(file)
                intent.data = uri
                context.sendBroadcast(intent)
                return true
            }
            return false
        } catch (e: Exception) {
            e.stackTrace
            return false
        }

    }

    /**
     * 尺寸压缩
     */
    fun compressSize(bitmap: Bitmap?) : Bitmap?{
        if(bitmap == null) return null

        val byteArray = bitmap2Bytes(bitmap) ?: return null
        // 配置压缩的参数
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true //获取当前图片的边界大小，而不是将整张图片载入在内存中，避免内存溢出
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)
        options.inJustDecodeBounds = false
        //inSampleSize的作用就是可以把图片的长短缩小inSampleSize倍，所占内存缩小inSampleSize的平方
        options.inSampleSize = caculateSampleSize(options, 500, 50)

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options) // 解码文件
    }

    /**
     * 计算出所需要压缩的大小
     * @param options
     * @param reqWidth  我们期望的图片的宽，单位px
     * @param reqHeight 我们期望的图片的高，单位px
     * @return
     */
    private fun caculateSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        var sampleSize = 1
        val picWidth = options.outWidth
        val picHeight = options.outHeight
        if (picWidth > reqWidth || picHeight > reqHeight) {
            val halfPicWidth = picWidth / 2
            val halfPicHeight = picHeight / 2
            while (halfPicWidth / sampleSize > reqWidth || halfPicHeight / sampleSize > reqHeight) {
                sampleSize *= 2
            }
        }
        return sampleSize
    }

    /**
     * 质量压缩
     */
    fun compressQuality(bitmap: Bitmap?, quality: Int = 100) : Bitmap?{
        if(bitmap == null) return null

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos)
        val byteArray = baos.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    /**
     * 把Bitmap转Byte
     */
    fun bitmap2Bytes(bm: Bitmap?): ByteArray? {
        if (null == bm) {
            return null
        }
		val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos)
		return baos.toByteArray();
    }

    fun path2Bytes(path: String?): ByteArray? {
        var bm = BitmapFactory.decodeFile(path)
        bm = rotateBitmap(bm, getBitmapDegree(path))

        return bitmap2Bytes(bm)
    }

    /**
     * 旋转图片，使图片保持正确的方向。
     *
     * @param bitmap  原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        if (degrees == 0 || null == bitmap) {
            return bitmap
        }
        val matrix = Matrix()
        matrix.setRotate(
            degrees.toFloat(),
            (bitmap.width / 2).toFloat(),
            (bitmap.height / 2).toFloat()
        )
        val bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return bmp
    }

    /**
     * 读取图片的旋转的角度
     *
     * @return 图片的旋转角度
     */
    fun getBitmapDegree(path: String?): Int {
        if(path == null) return 0
        var degree = 0
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            val exifInterface = ExifInterface(path)
            // 获取图片的旋转信息
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }

    /**
     * 把图片毛玻璃化
     */
    fun toBlur(originBitmap: Bitmap, scaleRatio: Int = 10): Bitmap? {
        // 增大scaleRatio缩放比，使用一样更小的bitmap去虚化可以到更好的得模糊效果，而且有利于占用内存的减小；
        var scaleRatio = scaleRatio
        val blurRadius = 8 //通常设置为8就行。
        //增大blurRadius，可以得到更高程度的虚化，不过会导致CPU更加intensive

        /* 其中前三个参数很明显，其中宽高我们可以选择为原图尺寸的1/10；
        第四个filter是指缩放的效果，filter为true则会得到一个边缘平滑的bitmap，
        反之，则会得到边缘锯齿、pixelrelated的bitmap。
        这里我们要对缩放的图片进行虚化，所以无所谓边缘效果，filter=false。*/
        if (scaleRatio <= 0) {
            scaleRatio = 10
        }
        var dstWidth = originBitmap.width / scaleRatio
        var dstHeight = originBitmap.height / scaleRatio
        val minSize = 50 //控制最小尺寸
        if (dstWidth <= dstHeight && dstWidth < minSize) {
            dstHeight = (dstHeight.toFloat() / dstWidth * minSize).toInt()
            dstWidth = minSize
        } else if (dstHeight < dstWidth && dstHeight < minSize) {
            dstWidth = (dstWidth.toFloat() / dstHeight * minSize).toInt()
            dstHeight = minSize
        }
        val scaledBitmap = Bitmap.createScaledBitmap(
            originBitmap,
            dstWidth,
            dstHeight,
            false
        )
        return doBlur(scaledBitmap, blurRadius, true)
    }

    private fun doBlur(sentBitmap: Bitmap, radius: Int, canReuseInBitmap: Boolean): Bitmap? {
        val bitmap: Bitmap
        bitmap = if (canReuseInBitmap) {
            sentBitmap
        } else {
            sentBitmap.copy(sentBitmap.config, true)
        }
        if (radius < 1) {
            return null
        }
        val w = bitmap.width
        val h = bitmap.height
        val pix = IntArray(w * h)
        bitmap.getPixels(pix, 0, w, 0, 0, w, h)
        val wm = w - 1
        val hm = h - 1
        val wh = w * h
        val div = radius + radius + 1
        val r = IntArray(wh)
        val g = IntArray(wh)
        val b = IntArray(wh)
        var rsum: Int
        var gsum: Int
        var bsum: Int
        var x: Int
        var y: Int
        var i: Int
        var p: Int
        var yp: Int
        var yi: Int
        var yw: Int
        val vmin = IntArray(Math.max(w, h))
        var divsum = div + 1 shr 1
        divsum *= divsum
        val dv = IntArray(256 * divsum)
        i = 0
        while (i < 256 * divsum) {
            dv[i] = i / divsum
            i++
        }
        yi = 0
        yw = yi
        val stack = Array(div) {
            IntArray(
                3
            )
        }
        var stackpointer: Int
        var stackstart: Int
        var sir: IntArray
        var rbs: Int
        val r1 = radius + 1
        var routsum: Int
        var goutsum: Int
        var boutsum: Int
        var rinsum: Int
        var ginsum: Int
        var binsum: Int
        y = 0
        while (y < h) {
            bsum = 0
            gsum = bsum
            rsum = gsum
            boutsum = rsum
            goutsum = boutsum
            routsum = goutsum
            binsum = routsum
            ginsum = binsum
            rinsum = ginsum
            i = -radius
            while (i <= radius) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))]
                sir = stack[i + radius]
                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff
                rbs = r1 - Math.abs(i)
                rsum += sir[0] * rbs
                gsum += sir[1] * rbs
                bsum += sir[2] * rbs
                if (i > 0) {
                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]
                } else {
                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]
                }
                i++
            }
            stackpointer = radius
            x = 0
            while (x < w) {
                r[yi] = dv[rsum]
                g[yi] = dv[gsum]
                b[yi] = dv[bsum]
                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum
                stackstart = stackpointer - radius + div
                sir = stack[stackstart % div]
                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm)
                }
                p = pix[yw + vmin[x]]
                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
                rsum += rinsum
                gsum += ginsum
                bsum += binsum
                stackpointer = (stackpointer + 1) % div
                sir = stack[stackpointer % div]
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
                rinsum -= sir[0]
                ginsum -= sir[1]
                binsum -= sir[2]
                yi++
                x++
            }
            yw += w
            y++
        }
        x = 0
        while (x < w) {
            bsum = 0
            gsum = bsum
            rsum = gsum
            boutsum = rsum
            goutsum = boutsum
            routsum = goutsum
            binsum = routsum
            ginsum = binsum
            rinsum = ginsum
            yp = -radius * w
            i = -radius
            while (i <= radius) {
                yi = Math.max(0, yp) + x
                sir = stack[i + radius]
                sir[0] = r[yi]
                sir[1] = g[yi]
                sir[2] = b[yi]
                rbs = r1 - Math.abs(i)
                rsum += r[yi] * rbs
                gsum += g[yi] * rbs
                bsum += b[yi] * rbs
                if (i > 0) {
                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]
                } else {
                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]
                }
                if (i < hm) {
                    yp += w
                }
                i++
            }
            yi = x
            stackpointer = radius
            y = 0
            while (y < h) {

                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] =
                    -0x1000000 and pix[yi] or (dv[rsum] shl 16) or (dv[gsum] shl 8) or dv[bsum]
                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum
                stackstart = stackpointer - radius + div
                sir = stack[stackstart % div]
                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w
                }
                p = x + vmin[y]
                sir[0] = r[p]
                sir[1] = g[p]
                sir[2] = b[p]
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
                rsum += rinsum
                gsum += ginsum
                bsum += binsum
                stackpointer = (stackpointer + 1) % div
                sir = stack[stackpointer]
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
                rinsum -= sir[0]
                ginsum -= sir[1]
                binsum -= sir[2]
                yi += w
                y++
            }
            x++
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h)
        return bitmap
    }

}