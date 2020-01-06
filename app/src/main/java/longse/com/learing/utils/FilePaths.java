package longse.com.learing.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * @author LY
 */
public class FilePaths {

    private Context mContext;

    public FilePaths(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 应用内部存储空间（数据文件私有），当我们的文件存储在这个文件下，是不需要申请权限的；
     * 当应用被卸载的时候目录下的文件也会跟着被删除。
     *
     * tips:这个文件的目录和应用存储的位置有关系，当我们的应用移动到外部存储空间的时候，文件的绝对路径是变化的
     * 所以我们在使用的时候尽量使用相对路径
     *
     * 这个目录和 getFilesDir() 不同之处就在于，当系统的存储空间比较少的时候，系统会自动删除这个目录下的文件。
     * 按照 google 官方的说法是，超过 1MB 的文件就最好存储在 getExternalCacheDir() 里面
     *
     * @return  输出路径为 /data/data/包名/cache
     */
    public File getCacheDir() {
        return mContext.getCacheDir();
    }


    /**
     * 应用内部存储空间（数据文件私有），当我们的文件存储在这个文件下，是不需要申请权限的；
     * 当应用被卸载的时候目录下的文件也会跟着被删除。
     *
     * tips:这个文件的目录和应用存储的位置有关系，当我们的应用移动到外部存储空间的时候，文件的绝对路径是变化的
     * 所以我们在使用的时候尽量使用相对路径
     *
     * 系统提供了一个访问该路径的方法： context.openFileOutput(String, int) or context.openFileInput(String, int)
     * @return  输出路径为 /data/data/包名/files
     */
    public File getFilesDir() {
        return mContext.getFilesDir();
    }


    /**
     * 应用内部存储空间（数据文件私有，系统媒体文件无法访问（例如存储了一个 MP3 文件，通过系统的文件夹管理系统，无法找到）），
     * 当应用被卸载的时候，目录下的文件会被删除，但是这里和 getCacheDir() 还有不同之处：
     * 只有手机烯烃使用的是虚拟外部存储（虚拟 SD 卡，现在绝大多数的手机，都不用外挂物理 SD 卡了）的时候，才可在卸载应用的同时，
     * 自动删除该目录下的文件，如果是之前的物理存储（物理 SD 卡）则不会自动删除该目录，及目录下的文件。
     *
     * 在使用的时候，需要判断外部的挂在状态（getExternalStorageState(file)）, 还需要申请读写权限（READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE）,
     *
     * tips: 当其他应用拥有 SD 卡读写权限的时候，可以访问该目录下的文件
     *
     * @return  输出路径为 /mnt/sdcard/Android/data/包名/cache
     */
    public File getExternalCacheDir() {
        return mContext.getExternalCacheDir();
    }

    /**
     * 应用外部存储空间（数据文件私有，系统媒体文件无法访问（例如存了一个 MP3 文件，通过系统的文件夹管理系统，无法找到）），
     * 当应用被卸载的时候，目录下的文件会被删除，但是这里和 getFilesDir() 还有不通之处：
     *
     * 只有手机系统使用的是虚拟外部存储（虚拟 SD 卡）的时候，
     * 才可以在卸载应用的同时，自动删除该目录下的文件，如果之前是物理存储（物理 SD 卡）则不会自动删除该目录，及目录下的文件
     *
     * 在使用的时候，需要判断外部存储的过载状态（getExternalStorageState(File)）, 还需要申请速写权限（READ_EXTERNAL_STORAGE， WRITE_EXTERNAL_STORAGE），
     *
     * tips：当其他应用拥有 SD 卡读写权限的时候，可以访问该目录下的文件
     * @return  输出路径为  /mnt/sdcard/Android/data/包名/files
     */
    public File getExternalFilesDir() {
        return mContext.getExternalFilesDir(null);
    }

    /**
     * 应用外部存储空间（数据文件非私有，可以被手机的系统程序访问（如 MP3 格式的文件，会被手机系统检测出来）， 同样所有的 App 程序也都是可以访问的）
     *
     * tips: 外部存储空间可能处于不可访问状态，或者已经被移除状态，或者存储空间损坏无法访问等问题。可以通过 getExternalStorageState()
     * 这个方法来判断外部存储空间的状态。
     *
     * 在该目录下读写文件，需要获取读写权限
     * 该目录下的文件，这个目录是用户进行操作的一个根目录，进入二级目录可以通过
     * getExternalFilesDirs(String), getExternalCacheDirs(), and getExternalMediaDirs() 这些方法
     *
     * 官方建议，不要直接使用该目录，为了避免污染用户的根命名空间，应用私有的数据，应该放在 Context.getExternalFilesDir 目录下其他的可以被分享的文件
     * 可以放在 getExternalStoragePublicDirectory(String) 目录下
     *
     * @return  输出路径为 /mnt/sdcard
     */
    public File getExternalStorageDirectory() {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 应用外部存储空间（数字文件非私有，可以被手机的系统程序访问），同样，所有的APP程序也都是可以访问的
     *
     * 这个目录是用来存放各种类型的文件的目录，在这里用户可以分类管理不同类的文件（如 音乐，图片。视频）
     * @return  输出路径为 /mnt/sdcard
     */
    public File getExternalStoragePublicDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
    }




























}
