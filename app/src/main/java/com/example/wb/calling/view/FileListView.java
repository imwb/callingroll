package com.example.wb.calling.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.wb.calling.R;
import com.lfk.justwetools.View.FileExplorer.OnFileChosenListener;
import com.lfk.justwetools.View.FileExplorer.OnFolderChosenListener;
import com.lfk.justwetools.View.FileExplorer.OnPathChangedListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by wb on 16/2/24.
 */
public class FileListView extends ListView {
    private String dir = Environment.getExternalStorageDirectory().getPath();
    private String root = Environment.getExternalStorageDirectory().getPath();
    private List<Map<String, Object>> fileList = new ArrayList();
    private OnFolderChosenListener folderChosenListener = null;
    private OnPathChangedListener pathChangedListener = null;
    private OnFileChosenListener fileChosenListener = null;
    private SimpleAdapter listAdapter = null;
    private Context context;
    private int[] Images;
   // public static final String[][] MIME_Table = new String[][]{{".3gp", "video/3gp"}, {".apk", "application/vnd.android.package-archive"}, {".asf", "video/x-ms-asf"}, {".avi", "video/x-msvideo"}, {".bin", "application/octet-stream"}, {".bmp", "image/bmp"}, {".c", "text/plain"}, {".class", "application/octet-stream"}, {".conf", "text/plain"}, {".cpp", "text/plain"}, {".doc", "application/msword"}, {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"}, {".xls", "application/vnd.ms-excel"}, {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}, {".exe", "application/octet-stream"}, {".gif", "image/gif"}, {".gtar", "application/x-gtar"}, {".gz", "application/x-gzip"}, {".h", "text/plain"}, {".htm", "text/html"}, {".html", "text/html"}, {".jar", "application/java-archive"}, {".java", "text/plain"}, {".jpeg", "image/jpeg"}, {".jpg", "image/jpeg"}, {".js", "application/x-javascript"}, {".log", "text/plain"}, {".m3u", "audio/x-mpegurl"}, {".m4a", "audio/mp4a-latm"}, {".m4b", "audio/mp4a-latm"}, {".m4p", "audio/mp4a-latm"}, {".m4u", "video/vnd.mpegurl"}, {".m4v", "video/x-m4v"}, {".mov", "video/quicktime"}, {".mp2", "audio/x-mpeg"}, {".mp3", "audio/x-mpeg"}, {".mp4", "video/mp4"}, {".mpc", "application/vnd.mpohun.certificate"}, {".mpe", "video/mpeg"}, {".mpeg", "video/mpeg"}, {".mpg", "video/mpeg"}, {".mpg4", "video/mp4"}, {".mpga", "audio/mpeg"}, {".msg", "application/vnd.ms-outlook"}, {".ogg", "audio/ogg"}, {".pdf", "application/pdf"}, {".png", "image/png"}, {".pps", "application/vnd.ms-powerpoint"}, {".ppt", "application/vnd.ms-powerpoint"}, {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"}, {".prop", "text/plain"}, {".rc", "text/plain"}, {".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"}, {".sh", "text/plain"}, {".tar", "application/x-tar"}, {".tgz", "application/x-compressed"}, {".txt", "text/plain"}, {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"}, {".wmv", "audio/x-ms-wmv"}, {".wps", "application/vnd.ms-works"}, {".xml", "text/plain"}, {".z", "application/x-compress"}, {".zip", "application/x-zip-compressed"}, {"", "*/*"}};
    public static final String[][] MIME_Table = new String[][]{{".xls", "application/vnd.ms-excel"}, {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}};

    public FileListView(Context context) {
        super(context);
        this.Images = new int[]{com.lfk.justwetools.R.drawable.file, com.lfk.justwetools.R.drawable.afile};
        this.initList(context);
    }

    public FileListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.Images = new int[]{com.lfk.justwetools.R.drawable.file, com.lfk.justwetools.R.drawable.afile};
        this.initList(context);
    }

    public FileListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.Images = new int[]{com.lfk.justwetools.R.drawable.file, com.lfk.justwetools.R.drawable.afile};
        this.initList(context);
    }

    private void initList(Context context) {
        this.context = context;
        this.listAdapter = new SimpleAdapter(context, this.fileList, com.lfk.justwetools.R.layout.list_item, new String[]{"image", "filename"}, new int[]{com.lfk.justwetools.R.id.list_image, com.lfk.justwetools.R.id.list_text});
        this.initListener();
        this.setAdapter(this.listAdapter);
        this.setCurrentDir(this.dir);
    }

    private void initListener() {
        this.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fileName = ((TextView)view.findViewById(R.id.list_text)).getText().toString();

                try {
                    File e = new File(FileListView.this.dir + "/" + fileName);
                    if(e.isFile()) {
                        if(FileListView.this.fileChosenListener == null) {
                            String mime = FileListView.this.getMIMEType(e);
                            if(mime != null) {
                                Intent intent = new Intent("android.intent.action.VIEW");
                                intent.setDataAndType(Uri.parse("file://" + e.getPath()), mime);
                                FileListView.this.context.startActivity(intent);
                            }
                        } else {
                            FileListView.this.fileChosenListener.onFileChosen(Uri.parse("file://" + e.getPath()));
                        }
                    } else if(e.isDirectory()) {
                        FileListView.this.setCurrentDir(e.getPath());
                    }
                } catch (Exception var10) {
                    var10.printStackTrace();
                }

            }
        });
    }


    public void setOnItemChosenListener(OnFolderChosenListener listener) {
        this.folderChosenListener = listener;
    }

    public void setOnPathChangedListener(OnPathChangedListener listener) {
        this.pathChangedListener = listener;
    }

    public void setOnFileChosenListener(OnFileChosenListener fileChosenListener) {
        this.fileChosenListener = fileChosenListener;
    }

    public String getType(File file) {
        if(file.isDirectory()) {
            return null;
        } else {
            int dotIndex = file.getName().lastIndexOf(".");
            return dotIndex < 0?"":file.getName().substring(dotIndex).toLowerCase();
        }
    }

    private String getMIMEType(File file) {
        String type = this.getType(file);
        if(type != null) {
            boolean found = false;
            String[][] arr$ = MIME_Table;
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String[] data = arr$[i$];
                if(type.equals(data[0])) {
                    type = data[1];
                    found = true;
                    break;
                }
            }

            if(!found) {
                type = "*/*";
            }
        }

        return type;
    }

    public void setCurrentDir(String path) {
        File[] files =  (new File(path)).listFiles();
        if(files != null) {
            this.dir = path;
            this.fileList.clear();
            File[] arr$ = files;
            int len$ = files.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                File file = arr$[i$];
                Hashtable fileInfo = new Hashtable();
                if(file.isFile()) {
                    fileInfo.put("image", Integer.valueOf(this.Images[1]));
                } else {
                    fileInfo.put("image", Integer.valueOf(this.Images[0]));
                }

                fileInfo.put("filename", file.getName());
                this.fileList.add(fileInfo);
            }

            this.listAdapter.notifyDataSetChanged();
            if(this.pathChangedListener != null) {
                this.pathChangedListener.onPathChanged(path);
            }

        }
    }

    public boolean toParentDir() {
        File file = new File(this.dir);
        String parentDir = file.getParent();
        if(parentDir != null && !file.getPath().equals(this.root)) {
            this.dir = parentDir;
            this.setCurrentDir(this.dir);
            return true;
        } else {
            return false;
        }
    }

    public void setRootDir(String rootDir) {
        this.root = rootDir;
    }

    public void refresh() {
        this.setCurrentDir(this.dir);
    }

    public String getCurrentPath() {
        return this.dir;
    }

    private void getProportion(TreeMap<String, Long> map, String path) throws Exception {
        File[] files = (new File(path)).listFiles();
        if(files == null) {
            throw new Exception("EMPTY OR ROOT FILE");
        } else {
            File[] arr$ = files;
            int len$ = files.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                File file = arr$[i$];
                if(file.isFile()) {
                    String type = this.getType(file).replace(".", "");
                    if(map.containsKey(type)) {
                        map.put(type, Long.valueOf(((Long)map.get(type)).longValue() + file.length()));
                    } else {
                        map.put(type, Long.valueOf(file.length()));
                    }
                } else if(file.isDirectory()) {
                    this.getProportion(map, file.getPath());
                }
            }

        }
    }

    public TreeMap<String, Long> getPropotionText(String path) throws Exception {
        TreeMap buf = new TreeMap();
        this.getProportion(buf, path);
        TreeMap map = new TreeMap(new FileListView.SizeComparator(buf));
        map.putAll(buf);
        return map;
    }

    class SizeComparator implements Comparator<String> {
        TreeMap<String, Long> map;

        SizeComparator(TreeMap<String, Long> var1) {
            this.map = map;
        }

        public int compare(String a, String b) {
            return ((Long)this.map.get(a)).equals(this.map.get(b))?0:(((Long)this.map.get(a)).longValue() > ((Long)this.map.get(b)).longValue()?1:-1);
        }
    }
}
