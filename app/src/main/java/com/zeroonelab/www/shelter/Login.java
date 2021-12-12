package com.zeroonelab.www.shelter;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    double windowWidth ,
            widthForBtnLogin,
            widthForBtnSignUp ;


    Toolbar toolbar ;
    Button btnLogin , btnSignUp;
    AutoCompleteTextView tiEmail ;
    EditText tiPassword ;
    ImageButton imgBtnTogglePassword ;

    FirebaseAuth auth ;
    ProgressDialog dialog ;

    ArrayAdapter<String> mailArrayAdapter ;

    Drawable enabledButtonDrawable , disabledButtonDrawable;
    String sharedEmail ;
    boolean checkPasswordVisibility = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        getWindowWidth() ;
        othersObject() ;
        sharedPreferencesChecker() ;
        uiObjectsInitializer() ;
        UDgetDrawableForDisabledButton(  ) ;
        toolbarInitialization() ;
        checkUserAlreadySignedIn() ;
        UserDefSetClickListenerOnBtn() ;
    }

    private void othersObject() {

        mailArrayAdapter = new ArrayAdapter<String>( this,android.R.layout.simple_list_item_1 ) ;
        dialog = new ProgressDialog( this,R.style.AppAlertDialogStyle  ) ;
    }

    private void checkUserAlreadySignedIn() {

        auth = FirebaseAuth.getInstance()  ;
        if(auth.getCurrentUser() != null)
        {
            startActivity( new Intent( Login.this, Bluetooth_Connect.class ) );
            finish();
        }

    }

    private void toolbarInitialization() {

        toolbar = findViewById( R.id.LoginToolbar ) ;
        setSupportActionBar( toolbar );
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle( "SIGN IN" );
        }
    }

    private void getWindowWidth() {

        windowWidth = Resources.getSystem().getDisplayMetrics().widthPixels  ;
        widthForBtnLogin = (windowWidth/2)-(225*1.5) ;
        widthForBtnSignUp = (windowWidth/2)-(225*1.5);
    }

    private void UDgetDrawableForDisabledButton() {

        enabledButtonDrawable = getResources().getDrawable( R.drawable.button_login ) ;
        disabledButtonDrawable = getResources().getDrawable( R.drawable.disabled_state_background ) ;
    }

    private void UserDefSetClickListenerOnBtn() {

        btnSignUp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity( new Intent( getApplicationContext() , CreateAccount.class ) );

            }
        } );

        tiEmail.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(tiPassword.getText().length() > 5 )
                {
                    enabledButtonState();
                }
                else
                {
                    disabledButtonState();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );

        tiPassword.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if( tiPassword.getText().length() > 5){

                    enabledButtonState();
                }
                else
                {
                    disabledButtonState();
                }
                int length = tiPassword.getText().toString().trim().length() ;

                if( length > 0){

                    imgBtnTogglePassword.setVisibility( View.VISIBLE );
                    if(checkPasswordVisibility){

                        imgBtnTogglePassword.setImageResource( R.drawable.ic_visibility_off );
                    }
                    else {

                        imgBtnTogglePassword.setImageResource( R.drawable.ic_remove_red_eye_black_24dp );
                    }
                }
                else {

                    imgBtnTogglePassword.setVisibility( View.INVISIBLE );
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );

        imgBtnTogglePassword.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int lengthOfPass = tiPassword.getText().toString().length() ;

                if(lengthOfPass>0) {

                    if (!checkPasswordVisibility) {

                        tiPassword.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                        imgBtnTogglePassword.setImageResource( R.drawable.ic_visibility_off );

                        checkPasswordVisibility = true;
                        tiPassword.setSelection( lengthOfPass );

                    } else{

                        tiPassword.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );

                        imgBtnTogglePassword.setImageResource( R.drawable.ic_remove_red_eye_black_24dp );

                        checkPasswordVisibility = false;
                        tiPassword.setSelection( lengthOfPass );

                    }

                }

            }
        } );

    }

    private void disabledButtonState()
    {
        btnLogin.setEnabled( false );
        btnLogin.setTextColor( Color.parseColor( "#e1f1e5e5" )  );
        btnLogin.setBackground( disabledButtonDrawable );
    }
    private void enabledButtonState()
    {
        btnLogin.setEnabled( true );
        btnLogin.setTextColor( getResources().getColor( android.R.color.white ) );
        btnLogin.setBackground( enabledButtonDrawable );
    }

    private void sharedPreferencesChecker() {

        SharedPreferences sharedPreferences = getSharedPreferences( "SHELTER",MODE_PRIVATE) ;

        String strEmail = sharedPreferences.getString( "EMAIL",null ) ;

        if(strEmail != null){

            sharedEmail = sharedPreferences.getString( "EMAIL","No name defined" ) ;
            mailArrayAdapter.add( sharedEmail );
        }

    }

    private void uiObjectsInitializer() {

        btnLogin = findViewById( R.id.LogInButton ) ;
        btnSignUp = findViewById( R.id.btnSignUp ) ;

        btnLogin.animate().translationX( (float)widthForBtnLogin ).setDuration( 1000 ).setListener( new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                disabledButtonState();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        } ) ;
        btnSignUp.animate().translationX( (float)widthForBtnSignUp ).setDuration( 1000 ).start();

        btnLogin.setEnabled( false );

        tiEmail = findViewById( R.id.LogInEmail ) ;
        tiEmail.setAdapter( mailArrayAdapter );

        imgBtnTogglePassword = findViewById( R.id.LogInImgBtnTogglePassword ) ;
        imgBtnTogglePassword.setVisibility( View.INVISIBLE );

        tiPassword = findViewById( R.id.LogInPassword ) ;
        tiPassword.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
        tiPassword.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        tiPassword.setSelection( tiPassword.length() );
    }

    public void SignIn(View view) {

        dialog.setMessage( "Please, Wait..." );
        dialog.show();
        dialog.setCanceledOnTouchOutside( false );
        if(dialog.getWindow() != null) {

            dialog.getWindow().setLayout( 800, WindowManager.LayoutParams.WRAP_CONTENT );
        }

        final String email = tiEmail.getText().toString() ;
        String password = tiPassword.getText().toString().trim() ;



        auth.signInWithEmailAndPassword( email, password ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        SharedPreferences.Editor editorShare = getSharedPreferences( "SHELTER",MODE_PRIVATE ).edit();
                        editorShare.putString( "EMAIL",email ) ;
                        editorShare.apply();

                        Intent intent = new Intent( getApplicationContext(), Bluetooth_Connect.class );
                        dialog.dismiss();
                        startActivity( intent );
                        finish();
                    }
                }
            } ).addOnFailureListener( new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    dialog.dismiss();
                    Toast.makeText( Login.this, e.toString().substring( 20 ).substring( 20 ), Toast.LENGTH_SHORT ).show();
                    tiEmail.setText( "" );
                    tiPassword.setText( "" );
                    return;
                }
            } );

    }

}
