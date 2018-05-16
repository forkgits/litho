/*
 * Copyright 2018-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.litho;

import android.support.annotation.Nullable;
import java.util.Map;

/**
 * This class provides utilities for extracting information through {@link
 * ComponentsLogger#getExtraAnnotations(TreeProps)} and transforming them so they can be logged.
 */
public final class LogTreePopulator {
  private LogTreePopulator() {}

  /**
   * Extract the treeprops from a given {@link ComponentContext} and convert them into perf event
   * annotations using a {@link ComponentsLogger} implementation.
   */
  public static void populatePerfEventFromLogger(
      ComponentContext c, ComponentsLogger logger, PerfEvent perfEvent) {
    @Nullable final TreeProps treeProps = c.getTreeProps();
    if (treeProps == null) {
      return;
    }

    @Nullable final Map<String, String> extraAnnotations = logger.getExtraAnnotations(treeProps);
    if (extraAnnotations == null) {
      return;
    }

    for (Map.Entry<String, String> e : extraAnnotations.entrySet()) {
      perfEvent.markerAnnotate(e.getKey(), e.getValue());
    }
  }

  /**
   * Turn the extracted tree props from a {@link ComponentsLogger} and turn them into a single
   * colon-separated string that
   *
   * @see #populatePerfEventFromLogger(ComponentContext, ComponentsLogger, PerfEvent)
   * @param component Component to extract tree props from.
   * @param logger
   * @return String of extracted props with key-value pairs separated by ':'.
   */
  @Nullable
  public static String getAnnotationBundleFromLogger(Component component, ComponentsLogger logger) {
    @Nullable final ComponentContext scopedContext = component.getScopedContext();
    if (scopedContext == null) {
      return null;
    }

    @Nullable final TreeProps treeProps = scopedContext.getTreeProps();
    if (treeProps == null) {
      return null;
    }

    @Nullable final Map<String, String> extraAnnotations = logger.getExtraAnnotations(treeProps);
    if (extraAnnotations == null) {
      return null;
    }

    final StringBuilder sb = new StringBuilder(extraAnnotations.size() * 16);
    for (Map.Entry<String, String> entry : extraAnnotations.entrySet()) {
      sb.append(entry.getKey());
      sb.append(':');
      sb.append(entry.getValue());
      sb.append(':');
    }

    return sb.toString();
  }
}