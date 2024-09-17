package com.mmar.pcreator;
import android.app.Activity;
import com.mmar.fm;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import com.mmar.util.dialogutil;
import android.app.Dialog;
import java.io.File;
import android.view.View;
import com.mmar.tts;
import android.widget.ArrayAdapter;
public class creator extends Activity
{Dialog d;
ListView list;
tts talk;
int id=0;
String lappname,lpname;
File mpackage;
File msrc,tres,temp,layout,values,xml;
File res,manifest,tmanifest;
EditText appName,pname;
ArrayAdapter ad;
	String smanifest="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\"\nandroid:versionCode=\"1\"\nandroid:versionName=\"1.0\"\npackage=\"";
String[] mlist=new String[]{"Normal app","Normal floating app","Widget"};
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creatorview);
		list=(ListView)findViewById(R.id.creatorviewList);
		ad=new ArrayAdapter(this,android.R.layout.simple_list_item_1,mlist);
		list.setAdapter(ad);
		talk=new tts(this);
		temp=new File("sdcard/app/temp");
	    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long lid) {
											id=position;
											d.setTitle(mlist[id]);
											d.show();
											
										}});
		d=dialogutil.dialog(this,R.layout.creatorinfoview);
		Button dyes=(Button)d.findViewById(R.id.creatoryes);
		Button dno=(Button)d.findViewById(R.id.creatorno);
		appName=(EditText)d.findViewById(R.id.cappName);
		pname=(EditText)d.findViewById(R.id.cpname);
		dyes.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					lappname=appName.getText().toString();
					lpname=pname.getText().toString();
					create();
					talk.toastShort("done");
					d.cancel();
				}
			});
		dno.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                d.cancel();
				}
			});
	}
	public void create(){
		createFolders();
		creatingManifest();
		copyres();
		copysrc();
	}
	public void createFolders(){
		mpackage=new File("sdcard/appprojects/"+lappname);
		msrc=new File(mpackage+"/src/"+lpname.replace(".","/"));
		res=new File(mpackage+"/res");
		layout=new File(res+"/layout");
		values=new File(res+"/values");
		xml=new File(res+"/xml");
		mpackage.mkdirs();
		msrc.mkdirs();
		layout.mkdirs();
		values.mkdirs();
		xml.mkdirs();
	}
	public void creatingManifest(){
		manifest=new File(mpackage+"/AndroidManifest.xml");
		tmanifest=new File("sdcard/app/temp/androidmanifest.xml");
		if(id==1){tmanifest=new File("sdcard/app/temp/swindow/androidmanifest.xml");}
		if(id==2){tmanifest=new File("sdcard/app/temp/widget/androidmanifest.xml");}
		//copying project.properties
		File mlib1=new File("sdcard/app/temp/project.properties");
		fm.copy(mlib1,mpackage,1);
		fm.save(manifest,smanifest+lpname+"\">\n"+fm.load(tmanifest));
	}
	public void copyres(){
		tres=new File("sdcard/app/temp/resn");
		if(id==2){
			fm.copy(new File("sdcard/app/temp/resn/drawable"),res,1);
			fm.copy(new File("sdcard/app/temp/reswidget/dwidget.xml"),layout,1);
			fm.copy(new File("sdcard/app/temp/reswidget/mwidgetdata.xml"),xml,1);
		}else{
		if(id==1){
			//copyig float single res
			fm.copy(new File("sdcard/app/temp/reswindow/swindow.xml"),layout,1);
			//copying drawable
			fm.copy(new File("sdcard/app/temp/resn/drawable"),res,1);
		}else{
			
		fm.copy(tres,res);}}
		String lsting="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>\n <string name=\"app_name\">"+lappname+"</string>\n</resources>";
		//saving string file
		fm.save(new File(res+"/values/strings.xml"),lsting);
	}
	public void copysrc(){
	    if(id==2){
			fm.save(new File(msrc+"/mwidget.java"),packagewriter(new File("sdcard/app/temp/widget/mwidget.java")));
		}else{
		if(id==1){
			fm.save(new File(msrc+"/MainActivity.java"),packagewriter(new File("sdcard/app/temp/swindow/MainActivity.java")));
			fm.save(new File(msrc+"/swindow.java"),packagewriter(new File("sdcard/app/temp/swindow/swindow.java")));
		}else{
		fm.save(new File(msrc+"/MainActivity.java"),packagewriter(new File("sdcard/app/temp/mainactivity.java")));}
	}}
	public String packagewriter(File file){
		return "package "+lpname+";\n"+fm.load(file);
	}
}
