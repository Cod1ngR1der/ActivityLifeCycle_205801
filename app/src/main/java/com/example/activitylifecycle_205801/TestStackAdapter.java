package com.example.activitylifecycle_205801;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;

public class TestStackAdapter extends StackAdapter<String> {
      public static Context context;
    public TestStackAdapter(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public void bindView(String data, int position, CardStackView.ViewHolder holder) {
        if (holder instanceof BankCard) {
            BankCard h = (BankCard) holder;
            h.onBind(data, position);
        }
        if (holder instanceof StuCard) {
            StuCard h = (StuCard) holder;
            h.onBind(data, position);
        }
        if (holder instanceof IdCard) {
            IdCard h = (IdCard) holder;
            h.onBind(data, position);
        }
    }

    @Override
    protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case R.layout.list_card_item_larger_header:
                view = getLayoutInflater().inflate(R.layout.list_card_item_larger_header, parent, false);
                return new BankCard(view);
            case R.layout.list_card_item_with_no_header:
                view = getLayoutInflater().inflate(R.layout.list_card_item_with_no_header, parent, false);
                return new StuCard(view);
            default:
                view = getLayoutInflater().inflate(R.layout.list_card_item, parent, false);
                return new IdCard(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (AndroidExpandingViewLibrary.TEST_DATAS[position].equals("身份证")) {
            return R.layout.list_card_item;
        } else if (AndroidExpandingViewLibrary.TEST_DATAS[position].equals("校园卡")) {
            return R.layout.list_card_item_with_no_header;
        } else {
            return R.layout.list_card_item_larger_header;
        }
    }

    static class IdCard extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextTitle;
        LinearLayout buttonDelete;
        LinearLayout buttonDetail;


        public IdCard(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mContainerContent = view.findViewById(R.id.container_list_content);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
            buttonDelete = view.findViewById(R.id.delete);
            buttonDetail=view.findViewById(R.id.detail);
        }

        @Override
        public void onItemExpand(boolean b) {
            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        @Override
        protected void onAnimationStateChange(int state, boolean willBeSelect) {
            super.onAnimationStateChange(state, willBeSelect);
            if (state == CardStackView.ANIMATION_STATE_START && willBeSelect) {
                onItemExpand(true);
            }
            if (state == CardStackView.ANIMATION_STATE_END && !willBeSelect) {
                onItemExpand(false);
            }
        }

        public void onBind(String data, int position) {
            //mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data), PorterDuff.Mode.SRC_IN);
            //mTextTitle.setText(String.valueOf(position));
            mTextTitle.setText(data);
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {showNormalDialog(v, data);}
            });
            buttonDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("详细信息");
                    System.out.println(position);
                    try {
                        Intent intent2 = new Intent(TestStackAdapter.context, Activity_IDCardDetail.class);
                        intent2.putExtra("idnum", AndroidExpandingViewLibrary.card_ids[position].getIdnum());
                        TestStackAdapter.context.startActivity(intent2);
    //            intent2.putExtra("snum", "202542");
    //            intent2.putExtra("idnum", "131127200203133412");TestStackAdapter.context.startActivity(intent2);
                    }catch (Exception e)
            {
                System.out.println(e);
            }
                }
            });
        }

    }

    static class StuCard extends CardStackView.ViewHolder {
        View mLayout;
        TextView mTextTitle;
        View mContainerContent;
        LinearLayout buttonDelete;
        LinearLayout buttonDetail;


        public StuCard(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mContainerContent = view.findViewById(R.id.container_list_content);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
            buttonDelete = view.findViewById(R.id.delete);
            buttonDetail=view.findViewById(R.id.detail);
        }

        @Override
        public void onItemExpand(boolean b) {
            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        @Override
        protected void onAnimationStateChange(int state, boolean willBeSelect) {
            super.onAnimationStateChange(state, willBeSelect);
            if (state == CardStackView.ANIMATION_STATE_START && willBeSelect) {
                onItemExpand(true);
            }
            if (state == CardStackView.ANIMATION_STATE_END && !willBeSelect) {
                onItemExpand(false);
            }
        }

        public void onBind(String data, int position) {
            //mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data), PorterDuff.Mode.SRC_IN);
            //mTextTitle.setText(String.valueOf(position));
            mTextTitle.setText(data);
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNormalDialog(v, data);
                }
            });
            buttonDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("校园卡position:"+position);
                    try {
                        Intent intent2 = new Intent(TestStackAdapter.context, Activity_studentCardDetail.class);
                        intent2.putExtra("snum", AndroidExpandingViewLibrary.card_students[position-AndroidExpandingViewLibrary.card_ids.length-AndroidExpandingViewLibrary.card_banks.length].getSnum());
                        TestStackAdapter.context.startActivity(intent2);
                        //            intent2.putExtra("snum", "202542");
                        //            intent2.putExtra("idnum", "131127200203133412");TestStackAdapter.context.startActivity(intent2);
                    }catch (Exception e)
                    {
                        System.out.println(e);
                    }
                }
            });
        }

    }

    static class BankCard extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextTitle;
        LinearLayout buttonDelete;
        LinearLayout buttonDetail;

        public BankCard(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mContainerContent = view.findViewById(R.id.container_list_content);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
            buttonDelete = view.findViewById(R.id.delete);
            buttonDetail=view.findViewById(R.id.detail);
        }

        @Override
        public void onItemExpand(boolean b) {
            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        @Override
        protected void onAnimationStateChange(int state, boolean willBeSelect) {
            super.onAnimationStateChange(state, willBeSelect);
            if (state == CardStackView.ANIMATION_STATE_START && willBeSelect) {
                onItemExpand(true);
            }
            if (state == CardStackView.ANIMATION_STATE_END && !willBeSelect) {
                onItemExpand(false);
            }
        }

        public void onBind(String data, int position) {
            //mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data), PorterDuff.Mode.SRC_IN);
            //mTextTitle.setText(String.valueOf(position));
            mTextTitle.setText(data);
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showNormalDialog(v, data);
                }
            });
            buttonDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("银行卡position："+position);
                    try {
                        Intent intent2 = new Intent(TestStackAdapter.context, Activity_bankCardDetail.class);
                        intent2.putExtra("number", AndroidExpandingViewLibrary.card_banks[position-AndroidExpandingViewLibrary.card_ids.length].getNumber());
                        TestStackAdapter.context.startActivity(intent2);
                        //            intent2.putExtra("snum", "202542");
                        //            intent2.putExtra("idnum", "131127200203133412");TestStackAdapter.context.startActivity(intent2);
                    }catch (Exception e)
                    {
                        System.out.println(e);
                    }
                }
            });

//            itemView.findViewById(R.id.text_view).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ((CardStackView) itemView.getParent()).performItemClick(BankCard.this);
//                }
//            });
        }

    }

    private static void showNormalDialog(View v, String data) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(v.getContext());
        normalDialog.setIcon(R.drawable.ic_delete);
        normalDialog.setTitle("删除卡片");
        normalDialog.setMessage("您确定要删除" + data + "吗？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }

}
