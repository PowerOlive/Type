/*
 * Copyright 2016 Matthew Lee
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.github.mthli.type.widget.holder;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;

import io.github.mthli.type.R;
import io.github.mthli.type.event.BlockEvent;
import io.github.mthli.type.event.BoldEvent;
import io.github.mthli.type.event.BulletEvent;
import io.github.mthli.type.event.DotsEvent;
import io.github.mthli.type.event.ImageEvent;
import io.github.mthli.type.event.InsertEvent;
import io.github.mthli.type.event.FormatEvent;
import io.github.mthli.type.event.ItalicEvent;
import io.github.mthli.type.event.QuoteEvent;
import io.github.mthli.type.event.StrikethroughEvent;
import io.github.mthli.type.event.UnderlineEvent;
import io.github.mthli.type.util.RxBus;
import io.github.mthli.type.widget.model.Type;
import io.github.mthli.type.widget.model.TypeBlock;
import io.github.mthli.type.widget.text.KnifeText;
import rx.functions.Action1;

public class TypeBlockHolder extends RecyclerView.ViewHolder {
    private View quote;
    private View bullet;
    private KnifeText content;
    private TypeBlock type;

    public TypeBlockHolder(@NonNull View view) {
        super(view);
        this.quote = view.findViewById(R.id.quote);
        this.bullet = view.findViewById(R.id.bullet);
        this.content = (KnifeText) view.findViewById(R.id.content);
        bind();
    }

    public void inject(TypeBlock type) {
        this.type = type;
        quote.setVisibility(type.isQuote() ? View.VISIBLE : View.GONE);
        bullet.setVisibility(type.isBullet() ? View.VISIBLE : View.GONE);
        content.setText(type.getContent());
    }

    private void bind() {
        RxView.focusChanges(content).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean hasFocus) {
                if (hasFocus) {
                    postBlockEvent();
                }
            }
        });

        RxTextView.afterTextChangeEvents(content).subscribe(new Action1<TextViewAfterTextChangeEvent>() {
            @Override
            public void call(TextViewAfterTextChangeEvent event) {
                if (type != null) {
                    type.setContent(event.editable());
                }
            }
        });

        content.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        postInsertEvent(Type.TYPE_BLOCK);
                    }

                    return true;
                }

                // TODO

                return false;
            }
        });

        RxBus.getInstance().toObservable(QuoteEvent.class)
                .subscribe(new Action1<QuoteEvent>() {
                    @Override
                    public void call(QuoteEvent event) {
                        if (content.hasFocus()) {
                            type.setBullet(type.isBullet() ? !type.isBullet() : type.isBullet());
                            type.setQuote(!type.isQuote());
                            inject(type);
                            postBlockEvent();
                        }
                    }
                });

        RxBus.getInstance().toObservable(BulletEvent.class)
                .subscribe(new Action1<BulletEvent>() {
                    @Override
                    public void call(BulletEvent event) {
                        if (content.hasFocus()) {
                            type.setQuote(type.isQuote() ? !type.isQuote() : type.isQuote());
                            type.setBullet(!type.isBullet());
                            inject(type);
                            postBlockEvent();
                        }
                    }
                });

        RxBus.getInstance().toObservable(BoldEvent.class)
                .subscribe(new Action1<BoldEvent>() {
                    @Override
                    public void call(BoldEvent event) {
                        if (content.hasFocus()) {
                            content.bold(!content.contains(KnifeText.FORMAT_BOLD));
                            type.setContent(content.getEditableText());
                            postFormatEvent();
                        }
                    }
                });

        RxBus.getInstance().toObservable(ItalicEvent.class)
                .subscribe(new Action1<ItalicEvent>() {
                    @Override
                    public void call(ItalicEvent event) {
                        if (content.hasFocus()) {
                            content.italic(!content.contains(KnifeText.FORMAT_ITALIC));
                            type.setContent(content.getEditableText());
                            postFormatEvent();
                        }
                    }
                });

        RxBus.getInstance().toObservable(UnderlineEvent.class)
                .subscribe(new Action1<UnderlineEvent>() {
                    @Override
                    public void call(UnderlineEvent event) {
                        if (content.hasFocus()) {
                            content.underline(!content.contains(KnifeText.FORMAT_UNDERLINE));
                            type.setContent(content.getEditableText());
                            postFormatEvent();
                        }
                    }
                });

        RxBus.getInstance().toObservable(StrikethroughEvent.class)
                .subscribe(new Action1<StrikethroughEvent>() {
                    @Override
                    public void call(StrikethroughEvent event) {
                        if (content.hasFocus()) {
                            content.strikethrough(!content.contains(KnifeText.FORMAT_STRIKETHROUGH));
                            type.setContent(content.getEditableText());
                            postFormatEvent();
                        }
                    }
                });

        // TODO link

        RxBus.getInstance().toObservable(DotsEvent.class)
                .subscribe(new Action1<DotsEvent>() {
                    @Override
                    public void call(DotsEvent event) {
                        if (content.hasFocus()) {
                            postInsertEvent(Type.TYPE_DOTS);
                        }
                    }
                });

        RxBus.getInstance().toObservable(ImageEvent.class)
                .subscribe(new Action1<ImageEvent>() {
                    @Override
                    public void call(ImageEvent event) {
                        if (content.hasFocus()) {
                            postInsertEvent(Type.TYPE_IMAGE, event.getBitmap());
                        }
                    }
                });
    }

    private void postBlockEvent() {
        BlockEvent event = new BlockEvent();
        event.setBullet(type.isBullet());
        event.setQuote(type.isQuote());
        RxBus.getInstance().post(event);
    }

    private void postFormatEvent() {
        FormatEvent event = new FormatEvent();
        event.setBold(content.contains(KnifeText.FORMAT_BOLD));
        event.setItalic(content.contains(KnifeText.FORMAT_ITALIC));
        event.setUnderline(content.contains(KnifeText.FORMAT_UNDERLINE));
        event.setStrikethrough(content.contains(KnifeText.FORMAT_STRIKETHROUGH));
        event.setLink(content.contains(KnifeText.FORMAT_LINK));
        RxBus.getInstance().post(event);
    }

    private void postInsertEvent(@Type.TypeValue int type) {
        postInsertEvent(type, null);
    }

    private void postInsertEvent(@Type.TypeValue int type, Bitmap bitmap) {
        Editable editable = content.getEditableText();
        int selectionStart = content.getSelectionStart();
        int selectionEnd = content.getSelectionEnd();
        int length = editable.length();
        Spanned prefix = new SpannableString(editable.subSequence(0, selectionStart));
        Spanned suffix = new SpannableString(editable.subSequence(selectionEnd, length));
        content.setText(prefix);
        RxBus.getInstance().post(new InsertEvent(type, getAdapterPosition(), prefix, suffix, bitmap));
    }
}
