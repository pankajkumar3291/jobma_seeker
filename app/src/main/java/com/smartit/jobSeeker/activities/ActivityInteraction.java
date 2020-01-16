package com.smartit.jobSeeker.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.chatApiResponses.Chatdatum;
import com.smartit.jobSeeker.apiResponse.chatApiResponses.CloseApiRequest;
import com.smartit.jobSeeker.apiResponse.chatApiResponses.EOChatHistory;
import com.smartit.jobSeeker.apiResponse.chatApiResponses.EOSendMessage;
import com.smartit.jobSeeker.apiResponse.handShakeForDashboard.HandshakeForDashboard;
import com.smartit.jobSeeker.eventbus.MessageEventProfile;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityInteraction extends AppCompatActivity {

    private RecyclerView recChat;
    private EditText etMessage;
    private List<Chatdatum> chatList = new ArrayList<>();
    private ChatAdapter chatAdapter;

    private int offset = 1;
    private Chatdatum chatdatum;
    private boolean isfirst = true;

    private LinearLayoutManager layoutManager;
    private int remainingCount, visibleItems = 1;
    private TextView spantext;
    private CheckBox checkBox;
    private ConstraintLayout etchatLayout;
    private ConstraintLayout textLayout;
    private Button btnResolved;
    private int contactID;
    private boolean fromessage = false;


    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;


    private EditText edReasionClosing;
    private Button btnOkay, btnCancel;
    private TextView tvWarning;

    private ProgressDialog mProgressDialog;

    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interaction);

        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        loadingProgress();
        Hawk.init(this).build();

        if (getIntent() != null && getIntent().hasExtra("contactId")) {
            contactID = this.getIntent().getIntExtra("contactId", 0);
        }


        chatAdapter = new ChatAdapter(chatList, this);
        layoutManager = new LinearLayoutManager(this);

        initView();
        getAllMessage();
        recyclerViewSetup();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void loadingProgress() {

        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


    }

    private void recyclerViewSetup() {

        recChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    if (!isLoading && layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == chatList.size() - 1) {
                        offset += 10;
                        isLoading = true;
                        loadingProgress();
                        getAllMessage();
                    }
                }
            }
        });


    }

    private void getAllMessage() {


        try {

            if (remainingCount > 0 || isfirst || fromessage) {
                fromessage = false;
                isfirst = false;

                HttpModule.provideRepositoryService().chatHistory(Hawk.get("GET_TOKEN"), contactID, offset, "10").enqueue(new Callback<EOChatHistory>() {
                    @Override
                    public void onResponse(Call<EOChatHistory> call, Response<EOChatHistory> response) {


                        mProgressDialog.dismiss();

                        if (response.body() != null) {
                            if (response.body().getError() == 0) {
                                if (response.body().getData().getTitle().getIssueStatus().equalsIgnoreCase("Complete")) {
                                    etchatLayout.setVisibility(View.GONE);
                                    btnResolved.setVisibility(View.VISIBLE);
                                    spantext.setVisibility(View.GONE);
                                }
                                if (response.body().getData().getChatdata() != null) {
                                    if (response.body().getData().getChatdata().size() > 0) {

                                        remainingCount = response.body().getData().getRemaining();
                                        chatList.addAll(response.body().getData().getChatdata());
                                        recChat.setAdapter(chatAdapter);
                                        chatAdapter.notifyDataSetChanged();
                                        isLoading = false;
                                    }
                                }
                            }
                        }
                    }


                    @Override
                    public void onFailure(Call<EOChatHistory> call, Throwable t) {
                        mProgressDialog.dismiss();
                    }
                });
            } else {
                mProgressDialog.dismiss();
            }

        } catch (Exception e) {
            mProgressDialog.dismiss();
            e.printStackTrace();
        }


    }

    private void initView() {

        findViewById(R.id.backbtnimg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.imageView73).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(etMessage.getText().toString().trim())) {
                    Toast.makeText(ActivityInteraction.this, "Please enter message", Toast.LENGTH_SHORT).show();
                } else {
                    loadingProgress();
                    sendMessage(etMessage.getText().toString());
                }
            }
        });

        btnResolved = findViewById(R.id.button24);
        recChat = findViewById(R.id.recchat);
        spantext = findViewById(R.id.textView211);
        checkBox = findViewById(R.id.checkBox3);
        textLayout = findViewById(R.id.readText);
        etchatLayout = findViewById(R.id.constraintLayout41);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {

                } else {

                }
            }
        });
        SpannableString spannable = new SpannableString("Your issue status is pending. Our support team will update it soon. Close issue");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
            }

            @Override
            public void onClick(View widget) {

                widget.invalidate();
                warningDilaog();

            }
        };
        spannable.setSpan(clickableSpan, 68, 79, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spantext.setText(spannable);
        spantext.setMovementMethod(LinkMovementMethod.getInstance());
        spantext.setHighlightColor(ContextCompat.getColor(this, R.color.colorLG));
//        downarrow = findViewById(R.id.floatingActionButton4);
        recChat.setHasFixedSize(true);
        recChat.setLayoutManager(layoutManager);
        etMessage = findViewById(R.id.editText19);


    }

    private void warningDilaog() {


        LayoutInflater li = LayoutInflater.from(ActivityInteraction.this);
        View dialogView = li.inflate(R.layout.dialog_close_chat, null);

        ids(dialogView);

        alertDialogBuilder = new AlertDialog.Builder(ActivityInteraction.this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();


    }

    private void ids(View dialogView) {


        edReasionClosing = dialogView.findViewById(R.id.edReasionClosing);
        btnOkay = dialogView.findViewById(R.id.btnOkay);
        btnCancel = dialogView.findViewById(R.id.btnCancel);
        tvWarning = dialogView.findViewById(R.id.tvWarning);
        tvWarning.setTypeface(tvWarning.getTypeface(), Typeface.BOLD);


        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!TextUtils.isEmpty(edReasionClosing.getText().toString().trim())) {
                    alertDialog.dismiss();
                    loadingProgress();
                    closeChatApi(edReasionClosing.getText().toString());


                } else {

                    Toast.makeText(ActivityInteraction.this, "Please enter the reason for closing the issues", Toast.LENGTH_SHORT).show();
//                    edReasionClosing.setError("Please enter the reason for closing the issues");
//                    edReasionClosing.requestFocus();
                }


            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void closeChatApi(String message) {


        try {
            HttpModule.provideRepositoryService().closeChat(Hawk.get("GET_TOKEN"), contactID, message).enqueue(new Callback<CloseApiRequest>() {
                @Override
                public void onResponse(Call<CloseApiRequest> call, Response<CloseApiRequest> response) {


                    mProgressDialog.dismiss();
                    if (response.body() != null) {


                        if (response.body().getError() == 0) {

                            ActivityInteraction.this.finish();
                        } else {

                            Toast.makeText(ActivityInteraction.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    } else {

                        Toast.makeText(ActivityInteraction.this, "Server error", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<CloseApiRequest> call, Throwable t) {

                    mProgressDialog.dismiss();
                    System.out.println("ActivityInteraction.onFailure");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mProgressDialog.dismiss();
            System.out.println("ActivityInteraction.closeChatApi");
        }


    }

    private void sendMessage(String message) {


        try {
            HttpModule.provideRepositoryService().sendChatMessage(Hawk.get("GET_TOKEN"), contactID, message).enqueue(new Callback<EOSendMessage>() {
                @Override
                public void onResponse(Call<EOSendMessage> call, Response<EOSendMessage> response) {


                    mProgressDialog.dismiss();
                    if (response.body() != null) {
                        if (response.body().getError() == 0) {
                            remainingCount = 0;
                            offset = 1;
                            chatList.clear();
                            etMessage.setText("");
                            fromessage = true;
                            getAllMessage();

                        } else {
                            chatAdapter.notifyDataSetChanged();
                        }
                    } else {
                    }

                }

                @Override
                public void onFailure(Call<EOSendMessage> call, Throwable t) {
                    mProgressDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mProgressDialog.dismiss();
        }


    }


    //todo  ====================================== Adapter Class ======================================
    public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
        private List<Chatdatum> chatList;
        private Context context;

        public ChatAdapter(List<Chatdatum> chatList, Context context) {
            this.chatList = chatList;
            this.context = context;
        }

        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_layout, viewGroup, false);
            return new ChatViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int i) {
            Chatdatum chatdatum = chatList.get(i);

            HttpModule.provideRepositoryService().handShakeForJobSeeker(Hawk.get("GET_TOKEN")).enqueue(new Callback<HandshakeForDashboard>() {
                @Override
                public void onResponse(Call<HandshakeForDashboard> call, Response<HandshakeForDashboard> response) {


                    if (response.body() != null && response.body().getError() == 0) {


                        if (TextUtils.isEmpty(response.body().getData().getImageurl())) {
                            Picasso.get()
                                    .load(R.drawable.no_image_available)
                                    .placeholder(R.drawable.no_image_available)
                                    .error(R.drawable.no_image_available)
                                    .into(chatViewHolder.circleImageView7);

                        } else {
                            Picasso.get()
                                    .load(response.body().getData().getImageurl())
                                    .placeholder(R.drawable.no_image_available)
                                    .error(R.drawable.no_image_available)
                                    .into(chatViewHolder.circleImageView7);
                        }


                    } else {

                        System.out.println("ChatAdapter.onResponse " + Objects.requireNonNull(response.body()).getMessage());
                    }

                }

                @Override
                public void onFailure(Call<HandshakeForDashboard> call, Throwable t) {

                    System.out.println("ChatAdapter.onFailure " + t.toString());
                }
            });


            if (chatdatum.getInteractionBy() == 0) {


                chatViewHolder.sendLayout.setVisibility(View.VISIBLE);
                chatViewHolder.reciveLayout.setVisibility(View.GONE);
                chatViewHolder.tvSendMessage.setText(chatdatum.getMessage());
                chatViewHolder.tvSendTime.setText(chatdatum.getUpdatedAt());
//                if (chatdatum.isProgress()) {
//                    chatViewHolder.progressBar.setVisibility(View.VISIBLE);
//                } else {
//                    chatViewHolder.progressBar.setVisibility(View.GONE);
//                }
//                if (chatdatum.isIsfailed()) {
//                    chatViewHolder.warning.setVisibility(View.VISIBLE);
//                } else {
//                    chatViewHolder.warning.setVisibility(View.GONE);
//                }
            } else {


                chatViewHolder.reciveLayout.setVisibility(View.VISIBLE);
                chatViewHolder.sendLayout.setVisibility(View.GONE);
                chatViewHolder.tvRecieveMessage.setText(chatdatum.getMessage());
                chatViewHolder.tvRecieveTime.setText(chatdatum.getUpdatedAt());
            }
        }

        @Override
        public int getItemCount() {
            return chatList.size();
        }

        class ChatViewHolder extends RecyclerView.ViewHolder {
            private TextView tvSendMessage, tvSendTime, tvRecieveMessage, tvRecieveTime;
            private ConstraintLayout sendLayout, reciveLayout;
            private ProgressBar progressBar;
            private ImageView warning;

            private CircleImageView circleImageView7;

            public ChatViewHolder(@NonNull View itemView) {
                super(itemView);

                tvSendMessage = itemView.findViewById(R.id.textView209);
                tvSendTime = itemView.findViewById(R.id.textView210);
                tvRecieveMessage = itemView.findViewById(R.id.textView2091);
                tvRecieveTime = itemView.findViewById(R.id.textView2101);
                sendLayout = itemView.findViewById(R.id.sendlayout);
                reciveLayout = itemView.findViewById(R.id.constraintLayout44);
                progressBar = itemView.findViewById(R.id.progressBar5);
                warning = itemView.findViewById(R.id.imageView80);
                circleImageView7 = itemView.findViewById(R.id.circleImageView7);
            }
        }
    }


}

