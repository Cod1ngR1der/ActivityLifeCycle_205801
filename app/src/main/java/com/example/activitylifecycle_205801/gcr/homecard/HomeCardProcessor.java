/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.example.activitylifecycle_205801.gcr.homecard;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.example.activitylifecycle_205801.entity.BlockItem;
import com.example.activitylifecycle_205801.entity.GeneralCardResult;
import com.example.activitylifecycle_205801.processor.gcr.GeneralCardProcessor;
import com.example.activitylifecycle_205801.util.StringUtils;
import com.huawei.hms.mlsdk.text.MLText;


import java.util.ArrayList;
import java.util.List;

/**
 * Post processing plug-in of Mainland Travel Permit for Hong Kong、Macao and Taiwan residents
 *
 * @since 2020-03-12
 */
public class HomeCardProcessor implements GeneralCardProcessor {
    private static final String TAG = "HomeCardProcessor";

    private final MLText text;

    public HomeCardProcessor(MLText text) {
        this.text = text;
    }

    @Override
    public GeneralCardResult getResult() {
        List<MLText.Block> blocks = text.getBlocks();
        if (blocks.isEmpty()) {
            Log.i(TAG, "Result blocks is empty");
            return null;
        }

        ArrayList<BlockItem> originItems = getOriginItems(blocks);

        String valid = "";
        String number = "";
        boolean numberFlag = false;
        boolean validFlag = false;

        for (BlockItem item : originItems) {
            String tempStr = item.text;

            if (!validFlag) {
                String result = tryGetValidDate(tempStr);
                if (!result.isEmpty()) {
                    valid = result;
                    validFlag = true;
                }
            }

            if (!numberFlag) {
                String result = tryGetCardNumber(tempStr);
                if (!result.isEmpty()) {
                    number = result;
                    numberFlag = true;
                }
            }
        }

        Log.i(TAG, "valid: " + valid);
        Log.i(TAG, "number: " + number);

        return new GeneralCardResult(valid, number);
    }

    private ArrayList<BlockItem> getOriginItems(List<MLText.Block> blocks) {
        ArrayList<BlockItem> originItems = new ArrayList<>();

        for (MLText.Block block : blocks) {
            // Add in behavior units
            List<MLText.TextLine> lines = block.getContents();
            for (MLText.TextLine line : lines) {
                String text = line.getStringValue();
                text = StringUtils.filterString(text, "[^a-zA-Z0-9\\.\\-,<\\(\\)\\s]");
                Log.d(TAG, "text: " + text);
                Point[] points = line.getVertexes();
                Rect rect = new Rect(points[0].x, points[0].y, points[2].x, points[2].y);
                BlockItem item = new BlockItem(text, rect);
                originItems.add(item);
            }
        }
        return originItems;
    }

    private String tryGetValidDate(String originStr) {
        return StringUtils.getCorrectValidDate(originStr);
    }

    private String tryGetCardNumber(String originStr) {
        return StringUtils.getHomeCardNumber(originStr);
    }
}