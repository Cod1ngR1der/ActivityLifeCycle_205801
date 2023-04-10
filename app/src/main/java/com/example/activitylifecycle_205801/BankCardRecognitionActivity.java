package com.example.activitylifecycle_205801;

import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huawei.hms.mlplugin.card.bcr.MLBcrCapture;
import com.huawei.hms.mlplugin.card.bcr.MLBcrCaptureConfig;
import com.huawei.hms.mlplugin.card.bcr.MLBcrCaptureFactory;
import com.huawei.hms.mlplugin.card.bcr.MLBcrCaptureResult;

import androidx.appcompat.app.AppCompatActivity;

public class BankCardRecognitionActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView frontImg;
    private ImageView frontSimpleImg;
    private ImageView frontDeleteImg;
    private LinearLayout frontAddView;
    private TextView showResult;
    private String lastFrontResult = "";
    private String lastBackResult = "";
    private Bitmap currentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card_recognition);

        initComponent();
    }

    private void initComponent() {
        frontImg = findViewById(R.id.avatar_img);
        frontSimpleImg = findViewById(R.id.avatar_sample_img);
        frontDeleteImg = findViewById(R.id.avatar_delete);
        frontAddView = findViewById(R.id.avatar_add);
        showResult = findViewById(R.id.show_result);

        frontAddView.setOnClickListener(this);
        frontDeleteImg.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.avatar_add:
                startCaptureActivity();
                break;
            case R.id.avatar_delete:
                showFrontDeleteImage();
                lastFrontResult = "";
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentImage != null && !currentImage.isRecycled()) {
            currentImage.recycle();
            currentImage = null;
        }
    }

    private void startCaptureActivity() {
        MLBcrCaptureConfig config = new MLBcrCaptureConfig.Factory()
                .setResultType(MLBcrCaptureConfig.RESULT_ALL)
                .setOrientation(MLBcrCaptureConfig.ORIENTATION_LANDSCAPE)
                .create();
        MLBcrCapture bcrCapture = MLBcrCaptureFactory.getInstance().getBcrCapture(config);

        bcrCapture.captureFrame(this, this.callback);
    }

    private String formatIdCardResult(MLBcrCaptureResult result) {
        StringBuilder resultBuilder = new StringBuilder();

        resultBuilder.append("银行卡号：");
        resultBuilder.append(result.getNumber());
        resultBuilder.append("\r\n");

        resultBuilder.append("发行人：");
        resultBuilder.append(result.getIssuer());
        resultBuilder.append("\r\n");

        resultBuilder.append("组织：");
        resultBuilder.append(result.getOrganization());
        resultBuilder.append("\r\n");

        resultBuilder.append("卡类型：");
        resultBuilder.append(result.getType());
        resultBuilder.append("\r\n");

        return resultBuilder.toString();
    }

    private MLBcrCapture.Callback callback = new MLBcrCapture.Callback() {
        @Override
        public void onSuccess(MLBcrCaptureResult result) {
            if (result == null) {
                return;
            }
            Bitmap bitmap = result.getOriginalBitmap();
            showSuccessResult(bitmap, result);
        }

        @Override
        public void onCanceled() {
        }

        @Override
        public void onFailure(int recCode, Bitmap bitmap) {
            showResult.setText(" RecFailed ");
        }

        @Override
        public void onDenied() {
        }
    };

    private void showSuccessResult(Bitmap bitmap, MLBcrCaptureResult idCardResult) {
        showFrontImage(bitmap);
        lastFrontResult = formatIdCardResult(idCardResult);
        showResult.setText(lastFrontResult);
        showResult.append(lastBackResult);
        ((ImageView) findViewById(R.id.number)).setImageBitmap(idCardResult.getNumberBitmap());
    }

    private void showFrontImage(Bitmap bitmap) {
        frontImg.setVisibility(View.VISIBLE);
        frontImg.setImageBitmap(bitmap);
        frontSimpleImg.setVisibility(View.GONE);
        frontAddView.setVisibility(View.GONE);
        frontDeleteImg.setVisibility(View.VISIBLE);
    }

    private void showFrontDeleteImage() {
        frontImg.setVisibility(View.GONE);
        frontSimpleImg.setVisibility(View.VISIBLE);
        frontAddView.setVisibility(View.VISIBLE);
        frontDeleteImg.setVisibility(View.GONE);
    }
}