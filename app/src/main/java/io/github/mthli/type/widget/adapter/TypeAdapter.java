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

package io.github.mthli.type.widget.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import io.github.mthli.type.R;
import io.github.mthli.type.widget.holder.TypeBlockHolder;
import io.github.mthli.type.widget.holder.TypeImageHolder;
import io.github.mthli.type.widget.holder.TypeTitleHolder;
import io.github.mthli.type.widget.model.Type;
import io.github.mthli.type.widget.model.TypeBlock;
import io.github.mthli.type.widget.model.TypeImage;
import io.github.mthli.type.widget.model.TypeTitle;

public class TypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<Type> list;

    public TypeAdapter(@NonNull Context context, @NonNull List<Type> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    @Type.TypeValue
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, @Type.TypeValue int type) {
        switch (type) {
            case Type.TYPE_BLOCK:
                return new TypeBlockHolder(inflater.inflate(R.layout.recycler_item_block, parent, false));
            case Type.TYPE_IMAGE:
                return new TypeImageHolder(inflater.inflate(R.layout.recycler_item_image, parent, false));
            case Type.TYPE_TITLE:
                return new TypeTitleHolder(inflater.inflate(R.layout.recycler_item_title, parent, false));
            default:
                return new TypeBlockHolder(inflater.inflate(R.layout.recycler_item_block, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Type type = list.get(position);

        if (holder instanceof TypeBlockHolder && type instanceof TypeBlock) {
            onBindTypeBlockHolder((TypeBlockHolder) holder, (TypeBlock) type);
        } else if (holder instanceof TypeImageHolder && type instanceof TypeImage) {
            onBindTypeImageHolder((TypeImageHolder) holder, (TypeImage) type);
        } else if (holder instanceof TypeTitleHolder && type instanceof TypeTitle) {
            onBindTypeTitleHolder((TypeTitleHolder) holder, (TypeTitle) type);
        }
    }

    private void onBindTypeBlockHolder(TypeBlockHolder holder, TypeBlock type) {
        // TODO
    }

    private void onBindTypeImageHolder(TypeImageHolder holder, TypeImage type) {
        // TODO
    }

    private void onBindTypeTitleHolder(TypeTitleHolder holder, TypeTitle type) {
        holder.title.setText(type.getTitle());
    }
}
