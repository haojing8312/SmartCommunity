package com.way.tabui.actity;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.way.tabui.actity.IntelligentSocketActivity.udpReceiveAndtcpSend;
import com.way.tabui.esptouch.task.__IEsptouchTask;
import com.way.tabui.esptouch.uti.EsptouchTask;
import com.way.tabui.esptouch.uti.IEsptouchListener;
import com.way.tabui.esptouch.uti.IEsptouchResult;
import com.way.tabui.esptouch.uti.IEsptouchTask;
import com.way.tabui.gokit.R;

public class EsptouchDemoActivity extends Activity implements OnClickListener {

	private static final String TAG = "EsptouchDemoActivity";

	private TextView mTvApSsid;

	private EditText mEdtApPassword;

	private Button mBtnConfirm;
	
	private ToggleButton mSwitchIsSsidHidden;

	private EspWifiAdminSimple mWifiAdmin;
	
//	private Spinner mSpinnerTaskCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.esptouch_demo_activity);

		Toast.makeText(getApplicationContext(),"设备配置有错或未连接成功，请检查设备或重新配置...", Toast.LENGTH_LONG).show();
		mWifiAdmin = new EspWifiAdminSimple(this);
		mTvApSsid = (TextView) findViewById(R.id.tvApSssidConnected);
		mEdtApPassword = (EditText) findViewById(R.id.edtApPassword);
		mBtnConfirm = (Button) findViewById(R.id.btnConfirm);
		mSwitchIsSsidHidden =  (ToggleButton) findViewById(R.id.switchIsSsidHidden);
		mBtnConfirm.setOnClickListener(this);
		//initSpinner();
		
	}
	
//	private void initSpinner()
	//{
//		mSpinnerTaskCount = (Spinner) findViewById(R.id.spinnerTaskResultCount);
//		int[] spinnerItemsInt = getResources().getIntArray(R.array.taskResultCount);
//		int length = spinnerItemsInt.length;
//		Integer[] spinnerItemsInteger = new Integer[length];
//		for(int i=0;i<length;i++)
//		{
//			spinnerItemsInteger[i] = spinnerItemsInt[i];
//		}
//		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,
//				android.R.layout.simple_list_item_1, spinnerItemsInteger);
//		mSpinnerTaskCount.setAdapter(adapter);
//		mSpinnerTaskCount.setSelection(1);
//	}

	@Override
	protected void onResume() {
		super.onResume();
		// display the connected ap's ssid
		String apSsid = mWifiAdmin.getWifiConnectedSsid();
		if (apSsid != null) {
			mTvApSsid.setText(apSsid);
		} else {
			mTvApSsid.setText("");
		}
		// check whether the wifi is connected
		boolean isApSsidEmpty = TextUtils.isEmpty(apSsid);
		mBtnConfirm.setEnabled(!isApSsidEmpty);
	}

	@Override
	public void onClick(View v) {

		if (v == mBtnConfirm) {
			String apSsid = mTvApSsid.getText().toString();
			String apPassword = mEdtApPassword.getText().toString();
			String apBssid = mWifiAdmin.getWifiConnectedBssid();
			Boolean isSsidHidden = mSwitchIsSsidHidden.isChecked();
			String isSsidHiddenStr = "NO";
//			String taskResultCountStr = Integer.toString(mSpinnerTaskCount
//					.getSelectedItemPosition());
			if (isSsidHidden) 
			{
				isSsidHiddenStr = "YES";
			}
			if (__IEsptouchTask.DEBUG) {
				Log.d(TAG, "mBtnConfirm is clicked, mEdtApSsid = " + apSsid
						+ ", " + " mEdtApPassword = " + apPassword);
			}
			new EsptouchAsyncTask3().execute(apSsid, apBssid, apPassword,
					isSsidHiddenStr, "1");
			
			time();
		}
	}
	
//	private class EsptouchAsyncTask2 extends AsyncTask<String, Void, IEsptouchResult> {
//
//		private ProgressDialog mProgressDialog;
//
//		private IEsptouchTask mEsptouchTask;
//		// without the lock, if the user tap confirm and cancel quickly enough,
//		// the bug will arise. the reason is follows:
//		// 0. task is starting created, but not finished
//		// 1. the task is cancel for the task hasn't been created, it do nothing
//		// 2. task is created
//		// 3. Oops, the task should be cancelled, but it is running
//		private final Object mLock = new Object();
//
//		@Override
//		protected void onPreExecute() {
//			mProgressDialog = new ProgressDialog(EsptouchDemoActivity.this);
//			mProgressDialog
//					.setMessage("Esptouch is configuring, please wait for a moment...");
//			mProgressDialog.setCanceledOnTouchOutside(false);
//			mProgressDialog.setOnCancelListener(new OnCancelListener() {
//				@Override
//				public void onCancel(DialogInterface dialog) {
//					synchronized (mLock) {
//						if (__IEsptouchTask.DEBUG) {
//							Log.i(TAG, "progress dialog is canceled");
//						}
//						if (mEsptouchTask != null) {
//							mEsptouchTask.interrupt();
//						}
//					}
//				}
//			});
//			mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE,
//					"Waiting...", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//						}
//					});
//			mProgressDialog.show();
//			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
//					.setEnabled(false);
//		}
//
//		@Override
//		protected IEsptouchResult doInBackground(String... params) {
//			synchronized (mLock) {
//				String apSsid = params[0];
//				String apBssid = params[1];
//				String apPassword = params[2];
//				String isSsidHiddenStr = params[3];
//				boolean isSsidHidden = false;
//				if (isSsidHiddenStr.equals("YES")) {
//					isSsidHidden = true;
//				}
//				mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword,
//						isSsidHidden, EsptouchDemoActivity.this);
//			}
//			IEsptouchResult result = mEsptouchTask.executeForResult();
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(IEsptouchResult result) {
//			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
//					.setEnabled(true);
//			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(
//					"Confirm");
//			// it is unnecessary at the moment, add here just to show how to use isCancelled()
//			if (!result.isCancelled()) {
//				if (result.isSuc()) {
//					mProgressDialog.setMessage("Esptouch success, bssid = "
//							+ result.getBssid() + ",InetAddress = "
//							+ result.getInetAddress().getHostAddress());
//				} else {
//					mProgressDialog.setMessage("Esptouch fail");
//				}
//			}
//		}
//	}
   
	private void onEsptoucResultAddedPerform(final IEsptouchResult result) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				String text = result.getBssid() + " is connected to the wifi";
				Toast.makeText(EsptouchDemoActivity.this, text,
						Toast.LENGTH_LONG).show();
			}

		});
	}

	private IEsptouchListener myListener = new IEsptouchListener() {

		@Override
		public void onEsptouchResultAdded(final IEsptouchResult result) {
			onEsptoucResultAddedPerform(result);
		}
	};
	
	private class EsptouchAsyncTask3 extends AsyncTask<String, Void, List<IEsptouchResult>> {

		private ProgressDialog mProgressDialog;

		private IEsptouchTask mEsptouchTask;
		// without the lock, if the user tap confirm and cancel quickly enough,
		// the bug will arise. the reason is follows:
		// 0. task is starting created, but not finished
		// 1. the task is cancel for the task hasn't been created, it do nothing
		// 2. task is created
		// 3. Oops, the task should be cancelled, but it is running
		private final Object mLock = new Object();

		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(EsptouchDemoActivity.this);
			mProgressDialog
					.setMessage("Esptouch is configuring, please wait for a moment...");
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					synchronized (mLock) {
						if (__IEsptouchTask.DEBUG) {
							Log.i(TAG, "progress dialog is canceled");
						}
						if (mEsptouchTask != null) {
							mEsptouchTask.interrupt();
						}
					}
				}
			});
			mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE,
					"Waiting...", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			mProgressDialog.show();
			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
					.setEnabled(false);
		}

		@Override
		protected List<IEsptouchResult> doInBackground(String... params) {
			int taskResultCount = -1;
			synchronized (mLock) {
				// !!!NOTICE
				String apSsid = mWifiAdmin.getWifiConnectedSsidAscii(params[0]);
				String apBssid = params[1];
				String apPassword = params[2];
				String isSsidHiddenStr = params[3];
				String taskResultCountStr = params[4];
				boolean isSsidHidden = false;
				if (isSsidHiddenStr.equals("YES")) {
					isSsidHidden = true;
				}
				taskResultCount = Integer.parseInt(taskResultCountStr);
				mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword,
						isSsidHidden, EsptouchDemoActivity.this);
				mEsptouchTask.setEsptouchListener(myListener);
			}
			List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
			return resultList;
		}

		@Override
		protected void onPostExecute(List<IEsptouchResult> result) {
			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
					.setEnabled(true);
			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(
					"Confirm");
			IEsptouchResult firstResult = result.get(0);
			// check whether the task is cancelled and no results received
			if (!firstResult.isCancelled()) {
				int count = 0;
				// max results to be displayed, if it is more than maxDisplayCount,
				// just show the count of redundant ones
				final int maxDisplayCount = 5;
				// the task received some results including cancelled while
				// executing before receiving enough results
				if (firstResult.isSuc()) {
					StringBuilder sb = new StringBuilder();
					for (IEsptouchResult resultInList : result) {
						sb.append("Esptouch success, bssid = "
								+ resultInList.getBssid()
								+ ",InetAddress = "
								+ resultInList.getInetAddress()
										.getHostAddress() + "\n");
						count++;
						if (count >= maxDisplayCount) {
							break;
						}
					}
					if (count < result.size()) {
						sb.append("\nthere's " + (result.size() - count)
								+ " more result(s) without showing\n");
					}
					mProgressDialog.setMessage(sb.toString());
				} else {
					mProgressDialog.setMessage("Esptouch fail"+firstResult.isSuc());
					mProgressDialog.cancel();
					Log.v("==", "firstResult.isSuc()"+firstResult.isSuc());
					Intent intent=new Intent(EsptouchDemoActivity.this,IntelligentSocketActivity.class);
					startActivity(intent);
					finish();
				}
			}
		}
		
	}


	  int  mTimer;
	   private Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1){
				if(mTimer==15){
					bisonclick=false;
					Toast.makeText(getApplicationContext(),"配置时间已过", Toast.LENGTH_SHORT).show();
					Intent intent=new Intent(EsptouchDemoActivity.this,IntelligentSocketActivity.class);
					startActivity(intent);
					finish();
//						Intent intent=new Intent(IntelligentSocketActivity.this,EsptouchDemoActivity.class);
//						startActivity(intent);
//						finish();
						
			
				}
				}
			};
		};
		
  Thread thread;
  private boolean bisonclick=true;
	public void time(){
		thread=new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				mTimer=0;
				while(bisonclick){
					Message mas= new Message();
					mas.what=1;
					mHandler.sendMessage(mas);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mTimer++;
					Log.v("==", "2time"+mTimer);
				}
			}
		});
		if (bisonclick) {
			thread.start();
		} else {
			thread.interrupt();
		}
	}
	
	
}
