package gdg.ninja.ui;

import gdg.nat.R;
import gdg.ninja.framework.BaseActivity;
import gdg.ninja.util.DebugLog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateCustomGameActivity extends BaseActivity implements
		OnClickListener{

	private static final int MEDIA_TYPE_IMAGE = 0;
	private final int GALLERY_PIC_REQUEST = 1;
	private final int CAMERA_PIC_REQUEST = 2;
	private final String IMG_HINT_URI = "saved_image";

	private TextView mTxtCreateButton;
	private TextView mTxtTakePictureButton;
	private TextView mTxtChoosePictureButton;
	
	private EditText mEditTxtNewWord;
	private Spinner mSpinChooseCategory;
	private ImageView mImgChoosedPicture;
	
	private Uri imagePath;

	private String TAG = "CREATE GAME ACTIVITY";

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_create_custom_game);
		initView();
	}
	
	/* Handle screen orientation changes */
	@Override
	protected void onSaveInstanceState(Bundle outState){
		if(imagePath != null && !imagePath.getPath().isEmpty())
			outState.putParcelable(IMG_HINT_URI, imagePath);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		imagePath = (Uri) savedInstanceState.getParcelable(IMG_HINT_URI);
		mImgChoosedPicture.setImageURI(imagePath);
		super.onRestoreInstanceState(savedInstanceState);
	}

	private void initView(){
		mTxtCreateButton = (TextView) findViewById(R.id.btn_create);
		mTxtTakePictureButton = (TextView) findViewById(R.id.btn_take_picture);
		mTxtChoosePictureButton = (TextView) findViewById(R.id.btn_choose_from_galary);
		mEditTxtNewWord = (EditText) findViewById(R.id.txt_input_new_word);
		mImgChoosedPicture = (ImageView) findViewById(R.id.img_input_hint_picture);
		mSpinChooseCategory = (Spinner) findViewById(R.id.spinner_choose_category);
		
		// Set on click listener
		mTxtCreateButton.setOnClickListener(this);
		mTxtTakePictureButton.setOnClickListener(this);
		mTxtChoosePictureButton.setOnClickListener(this);
		
		// Get category list and put into Spinner
		initSpinner();
	}
	
	private void initSpinner(){
		// TODO: Implement query to database and put category list into spinner
	}
	
	/* Validate data and create new game */
	private void createGame(){
		String new_word = mEditTxtNewWord.getText().toString();
		if(isDataValidated(new_word, imagePath, "Default")){
			DebugLog.i(TAG, "Word: " + new_word + " Image Path: " + imagePath);
			// TODO: Implement save new word to database
		}
	}
	
	private boolean isDataValidated(String newWord, Uri imagePath,
			String Category){
		/* Validate input new word */
		if(newWord.equals("") || newWord == null){ // Check for blank input
			mEditTxtNewWord.setError(getString(R.string.error_input_blank));
			mEditTxtNewWord.requestFocus();
			return false;
		}
		// else if (false){ // TODO: Check new word already exist!
		// mEditTxtNewWord.setError(getString(R.string.error_input_already_exist));
		// mEditTxtNewWord.requestFocus();
		// return false;
		// }
		
		/* Validate image path */
		// TODO: implement validate image
		if(imagePath == null || imagePath.getPath().isEmpty()){
			// Set ImageView to some image which cute and can remind user to
			// input image
			return false;
		}
		
		/* Validate for category */
		// TODO: Implement validate for category, it should exist!
		
		// If everything is ok, return true
		return true;
	}
	
	/* Choose picture from Gallery and put into mImgChoosedPicture */
	private void choosePictureFromGallery(){
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent,
				getString(R.string.intent_select_picture)), GALLERY_PIC_REQUEST);
	}
	
	/* Helper function for Take Picture from camera: create file name */
	private static File getOutputMediaFile(int type){
		// For future implementation: store videos in a separate directory
		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory(), "NinjaLearnJapanese");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.
		
		// Create the storage directory if it does not exist
		if(!mediaStorageDir.exists()){
			if(!mediaStorageDir.mkdirs()){
				DebugLog.d("Create new game", "failed to create directory");
				return null;
			}
		}
		
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
				.format(new Date());
		File mediaFile;
		if(type == MEDIA_TYPE_IMAGE){
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		}else{
			DebugLog.e("Create new game",
					"type of media file not supported: type was:" + type);
			return null;
		}
		
		return mediaFile;
	}
	
	/* Take picture from camera */
	private void takePictureFromCamera(){
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		File newFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
		imagePath = Uri.fromFile(newFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePath);
		startActivityForResult(intent, CAMERA_PIC_REQUEST);
	}
	
	/* Receive image from Camera or Gallery */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK){
			if(requestCode == GALLERY_PIC_REQUEST){
				imagePath = data.getData();
			}else if(requestCode == CAMERA_PIC_REQUEST){
				
			}
			
			DebugLog.i(TAG, "image Path: " + imagePath);
			if(imagePath != null) mImgChoosedPicture.setImageURI(imagePath);
		}
	}

	@Override
	public void onClick(View view){
		int viewID = view.getId();
		switch(viewID){
			case R.id.btn_create:
				createGame();
				break;
			case R.id.btn_take_picture:
				takePictureFromCamera();
				break;
			case R.id.btn_choose_from_galary:
				choosePictureFromGallery();
				break;
			default:
				break;
		}
	}

}