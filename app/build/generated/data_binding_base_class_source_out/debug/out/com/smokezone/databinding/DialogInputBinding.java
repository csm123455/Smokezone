// Generated by view binder compiler. Do not edit!
package com.smokezone.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.smokezone.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogInputBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView add;

  @NonNull
  public final TextView remove;

  @NonNull
  public final EditText smokeCount;

  private DialogInputBinding(@NonNull LinearLayout rootView, @NonNull TextView add,
      @NonNull TextView remove, @NonNull EditText smokeCount) {
    this.rootView = rootView;
    this.add = add;
    this.remove = remove;
    this.smokeCount = smokeCount;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogInputBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogInputBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_input, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogInputBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.add;
      TextView add = ViewBindings.findChildViewById(rootView, id);
      if (add == null) {
        break missingId;
      }

      id = R.id.remove;
      TextView remove = ViewBindings.findChildViewById(rootView, id);
      if (remove == null) {
        break missingId;
      }

      id = R.id.smokeCount;
      EditText smokeCount = ViewBindings.findChildViewById(rootView, id);
      if (smokeCount == null) {
        break missingId;
      }

      return new DialogInputBinding((LinearLayout) rootView, add, remove, smokeCount);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
