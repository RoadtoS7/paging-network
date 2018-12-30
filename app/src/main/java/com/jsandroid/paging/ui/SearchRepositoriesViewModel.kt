/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jsandroid.paging.ui

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.jsandroid.paging.data.GithubRepository
import com.jsandroid.paging.model.Repo
import com.jsandroid.paging.model.RepoSearchResult


/**
 * ViewModel for the [SearchRepositoriesActivity] screen.
 * The ViewModel works with the [GithubRepository] to get the data.
 */
class SearchRepositoriesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GithubRepository = GithubRepository(application)

    private val queryLiveData = MutableLiveData<String>()
    private val repoResult: LiveData<RepoSearchResult> = Transformations.map(queryLiveData, {
        repository.search(it)
    })

    val repos: LiveData<PagedList<Repo>> = Transformations.switchMap(repoResult,
            { it -> it.data })
    val networkErrors: LiveData<String> = Transformations.switchMap(repoResult,
            { it -> it.networkErrors })

    /**
     * Search a repository based on a query string.
     */
    fun searchRepo(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    /**
     * Get the last query value.
     */
    fun lastQueryValue(): String? = queryLiveData.value

}