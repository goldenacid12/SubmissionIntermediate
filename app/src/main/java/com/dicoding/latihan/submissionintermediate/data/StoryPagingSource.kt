package com.dicoding.latihan.submissionintermediate.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.latihan.submissionintermediate.api.ApiService
import com.dicoding.latihan.submissionintermediate.model.UserModel
import com.dicoding.latihan.submissionintermediate.response.ListStoryItem
import java.lang.Exception


class StoryPagingSource(private val apiService: ApiService): PagingSource<Int, ListStoryItem>() {

    private lateinit var userModel: UserModel

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try{
            val page = params.key ?: INITIAL_PAGE_INDEX
            val token = userModel.token
            Log.d("TOKEN", token)
            val responseData = apiService.getStories("bearer $token", page, params.loadSize)

            LoadResult.Page(
                data = responseData as MutableList<ListStoryItem>,
                prevKey = if(page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if(responseData.isNullOrEmpty()) null else page + 1
            )
        }catch (exception: Exception){
            return LoadResult.Error(exception)
        }
    }

    private companion object{
        const val INITIAL_PAGE_INDEX = 1
    }

}