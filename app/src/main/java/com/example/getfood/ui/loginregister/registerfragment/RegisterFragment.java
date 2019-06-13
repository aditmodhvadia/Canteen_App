package com.example.getfood.ui.loginregister.registerfragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.getfood.R;
import com.example.getfood.ui.base.BaseFragment;
import com.example.getfood.ui.terms.TermsActivity;
import com.example.getfood.utils.AlertUtils;
import com.example.getfood.utils.DialogSimple;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterFragment extends BaseFragment implements RegisterMvpView, View.OnClickListener {

    private EditText userConPasswordEditText, userPasswordEditText, userEmailEditText;
    private CheckBox termsCheckBox;
    private RegisterPresenter<RegisterFragment> presenter;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_register;
    }

    @Override
    public void initViews(View view) {
        getActivity().setTitle(R.string.register);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        presenter = new RegisterPresenter<>();
        presenter.onAttach(this);

        userConPasswordEditText = view.findViewById(R.id.userConPasswordEditText);
        userPasswordEditText = view.findViewById(R.id.userPasswordEditText);
        userEmailEditText = view.findViewById(R.id.userLoginEmailEditText);
        termsCheckBox = view.findViewById(R.id.termsCheckBox);

    }

    @Override
    public void setListeners(View view) {
        view.findViewById(R.id.userAddButton).setOnClickListener(this);
        view.findViewById(R.id.termsTextView).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userAddButton:
                if (presenter.isTermsAndConditionChecked(termsCheckBox.isChecked())) {
                    showLoading();
//                TODO: Hide Keyboard
//                userEmailEditText.setText("");
                    String confirmPassword = userConPasswordEditText.getText().toString().trim();
                    String password = userPasswordEditText.getText().toString().trim();
                    userConPasswordEditText.setText("");
                    userPasswordEditText.setText("");

                    presenter.performRegistration(userEmailEditText.getText().toString().trim(),
                            password, confirmPassword);
//                presenter.performRegistration("adit.modhvadia@gmail.com", "12345678", "12345678");
                } else {
                    AlertUtils.showAlertBox(mContext, getString(R.string.warning),
                            getString(R.string.accept_terms_and_condition), getString(R.string.ok), new DialogSimple.AlertDialogListener() {
                                @Override
                                public void onButtonClicked() {
                                    termsCheckBox.requestFocus();
                                }
                            });
                }

                break;
            case R.id.termsTextView:
                startActivity(new Intent(getContext(), TermsActivity.class));
                break;
        }
    }

    @Override
    public void onUserCreatedSuccessfully() {
        showLoading();
        presenter.verifyUserEmail();
    }

    @Override
    public void userVerifiedSuccessfully() {
        hideLoading();

        AlertUtils.showAlertBox(mContext, getString(R.string.email_sent), "Verification Email sent to your account. Check your Email",
                getString(R.string.yes), new DialogSimple.AlertDialogListener() {
                    @Override
                    public void onButtonClicked() {
                        Toast.makeText(getContext(), "Login again after verification", Toast.LENGTH_LONG).show();
                        userEmailEditText.setText("");
                        userPasswordEditText.setText("");
                        userConPasswordEditText.setText("");
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            Log.d("##DebugData", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        } else {
                            Log.d("##DebugData", "User null in register itself");
                        }
//                        presenter.signOutUser();
//                        todo: redirect to login fragment
                    }
                });
    }

    @Override
    public void onUserEmailVerificationFailed() {
        hideLoading();
//        todo: Show alert dialog for retry;
    }

    @Override
    public void valueEntryError(String errorMsg) {
        hideLoading();
        presenter.signOutUser();
        AlertUtils.showAlertBox(mContext, getString(R.string.error_occurred), errorMsg, getString(R.string.ok), new DialogSimple.AlertDialogListener() {
            @Override
            public void onButtonClicked() {
                userEmailEditText.requestFocus();
            }
        });
    }
}