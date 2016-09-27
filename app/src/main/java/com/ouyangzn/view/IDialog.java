/*
 * Copyright (c) 2016.  ouyangzn   <email : ouyangzn@163.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ouyangzn.view;

import android.view.View;
import com.orhanobut.dialogplus.DialogPlus;

public interface IDialog {
  /**
   * Interface used to allow the creator of a dialog to run some code when an
   * item on the dialog is clicked..
   */
  public interface OnClickListener {
    void onClick(DialogPlus dialog, View view);
  }

  public interface OnChooseListener {
    void onChoose(DialogPlus dialog, View view, Object o);
  }
}
