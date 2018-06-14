package com.pingxundata.pxcore.autoupdate;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;

import com.pingxun.library.commondialog.CommomDialog;

class UpdateDialog {


    static void show(final Context context, String content, final String downloadUrl) {
        if (isContextValid(context)) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle(R.string.android_auto_update_dialog_title);
//            builder.setMessage(Html.fromHtml(content))
//                    .setPositiveButton(R.string.android_auto_update_dialog_btn_download, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            goToDownload(context, downloadUrl);
//                        }
//                    })
//                    .setNegativeButton(R.string.android_auto_update_dialog_btn_cancel, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                        }
//                    });
//
//            AlertDialog dialog = builder.create();
//            //点击对话框外面,对话框不消失
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.show();

            //弹出提示框
            new CommomDialog(context, com.pingxun.library.R.style.dialog,content, new CommomDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    if(confirm){
                        goToDownload(context, downloadUrl);
                        dialog.dismiss();
                    }
                }
            }).setTitle("版本升级").setContentPosition(Gravity.LEFT).show();

            //弹窗是否需要更新
//            DialogBuilderParam dialogParam1 = new DialogBuilderParam();
//            dialogParam1.setFunction1(new IFunctionParam() {
//                @Override
//                public void doFunctiosOne() {
//                    goToDownload(context, downloadUrl);
//                    DiaLogBulder.dismissNotifyDialog(context);
//                }
//
//                @Override
//                public void doFunctionWithParams(Object objects) {
//
//                }
//
//                @Override
//                public void doFunctiosTwo() {
//                    DiaLogBulder.dismissNotifyDialog(context);
//                }
//            });
//            dialogParam1.setB1Text("确定");
//            dialogParam1.setB2Text("取消");
//            dialogParam1.setmButtom1Color("#F3422E");
//            dialogParam1.setmButtom2Color("#666666");
//            dialogParam1.setButtonContentColor("#FFFFFF");
//            dialogParam1.setWithButom1TextColor("#FFFFFF");
//            dialogParam1.setWithButom2TextColor("#FFFFFF");
//            dialogParam1.setTopViewColor("#F3422E");
//            dialogParam1.setContentPanelColor("#FFFFFF");
//            dialogParam1.setDialogColor("#FFFFFF");
//            dialogParam1.setDividerColor("#FFE74C3C");
//            dialogParam1.setMessage("发现新版本，是否更新");
//            dialogParam1.setMessageColor("#666666");
//            dialogParam1.setTitle("更新");
//            dialogParam1.setTitleColor("#FFFFFF");
//            DiaLogBulder.showNotifyDialog(context,dialogParam1);
        }
    }

    private static boolean isContextValid(Context context) {
        return context instanceof Activity && !((Activity) context).isFinishing();
    }


    private static void goToDownload(Context context, String downloadUrl) {
        Intent intent = new Intent(context.getApplicationContext(), DownloadService.class);
        intent.putExtra(Constants.APK_DOWNLOAD_URL, downloadUrl);
        context.startService(intent);
    }
}
