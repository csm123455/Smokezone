// Generated by view binder compiler. Do not edit!
package com.smokezone.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.smokezone.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityPostBinding implements ViewBinding {
  @NonNull
  private final NestedScrollView rootView;

  @NonNull
  public final EditText commentEditText;

  @NonNull
  public final RecyclerView commentsRecyclerView;

  @NonNull
  public final TextView contentTextView;

  @NonNull
  public final ImageView noCommentImageView;

  @NonNull
  public final LinearLayout sendContainer;

  @NonNull
  public final TextView titleTextView;

  @NonNull
  public final Button writeBtn;

  private ActivityPostBinding(@NonNull NestedScrollView rootView, @NonNull EditText commentEditText,
      @NonNull RecyclerView commentsRecyclerView, @NonNull TextView contentTextView,
      @NonNull ImageView noCommentImageView, @NonNull LinearLayout sendContainer,
      @NonNull TextView titleTextView, @NonNull Button writeBtn) {
    this.rootView = rootView;
    this.commentEditText = commentEditText;
    this.commentsRecyclerView = commentsRecyclerView;
    this.contentTextView = contentTextView;
    this.noCommentImageView = noCommentImageView;
    this.sendContainer = sendContainer;
    this.titleTextView = titleTextView;
    this.writeBtn = writeBtn;
  }

  @Override
  @NonNull
  public NestedScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityPostBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityPostBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_post, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityPostBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.commentEditText;
      EditText commentEditText = ViewBindings.findChildViewById(rootView, id);
      if (commentEditText == null) {
        break missingId;
      }

      id = R.id.commentsRecyclerView;
      RecyclerView commentsRecyclerView = ViewBindings.findChildViewById(rootView, id);
      if (commentsRecyclerView == null) {
        break missingId;
      }

      id = R.id.contentTextView;
      TextView contentTextView = ViewBindings.findChildViewById(rootView, id);
      if (contentTextView == null) {
        break missingId;
      }

      id = R.id.noCommentImageView;
      ImageView noCommentImageView = ViewBindings.findChildViewById(rootView, id);
      if (noCommentImageView == null) {
        break missingId;
      }

      id = R.id.sendContainer;
      LinearLayout sendContainer = ViewBindings.findChildViewById(rootView, id);
      if (sendContainer == null) {
        break missingId;
      }

      id = R.id.titleTextView;
      TextView titleTextView = ViewBindings.findChildViewById(rootView, id);
      if (titleTextView == null) {
        break missingId;
      }

      id = R.id.writeBtn;
      Button writeBtn = ViewBindings.findChildViewById(rootView, id);
      if (writeBtn == null) {
        break missingId;
      }

      return new ActivityPostBinding((NestedScrollView) rootView, commentEditText,
          commentsRecyclerView, contentTextView, noCommentImageView, sendContainer, titleTextView,
          writeBtn);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
