package com.gershad.gershad.network

import android.content.Context
import com.gershad.gershad.model.*
import java.util.Calendar
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Flowable
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

/**
 * Retrieve and dispatch data to and from services [ApiService] network).
 */
interface Repository {
    fun getReports(reportBounds: ReportBounds): Flowable<List<ReportGroup>>
    fun getReports(latlng: LatLng): Flowable<List<ReportGroup>>
    fun postReport(report: Report): Flowable<Report>
    fun deleteReport(report: Report): Flowable<Response<Void>>
    fun updateReport(report: Report): Flowable<Report>
    fun getPoi(token: String): Flowable<List<SavedLocation>>
    fun postPoi(report: SavedLocation): Flowable<SavedLocation>
    fun deletePoi(deleteRequest: DeleteRequest): Flowable<Response<Void>>
}

/**
 * Retrieve and dispatch data to and from services [ApiService] network).
 *
 * @param   context     Context
 * @param   apiService  Implementation of network endpoint contract
 *
 */
class RepositoryImpl(val context: Context, private val apiService: ApiService) : Repository {

    /*
     * Report details with clustering and bounding box
     */
    override fun getReports(reportBounds: ReportBounds): Flowable<List<ReportGroup>> {
        return apiService.getReports(reportBounds.toQueryMap())
    }

    override fun getReports(latlng: LatLng): Flowable<List<ReportGroup>> {
        return apiService.getReports(latlng.latitude, latlng.longitude)
    }

    override fun postReport(report: Report): Flowable<Report> {
        report.requestTime = getISODateTime()
        return apiService.postReport(report)
    }

    override fun updateReport(report: Report): Flowable<Report> {
        report.requestTime = getISODateTime()
        return apiService.updateReport(report)
    }

    override fun deleteReport(report: Report): Flowable<Response<Void>> {
        report.requestTime = getISODateTime()
        return apiService.deleteReport(DeleteRequest(report))
    }

    override fun getPoi(token: String): Flowable<List<SavedLocation>> {
        return apiService.getPoi(token)
    }

    override fun postPoi(report: SavedLocation): Flowable<SavedLocation> {
        report.requestTime = getISODateTime()
        return apiService.postPoi(report)
    }

    override fun deletePoi(deleteRequest: DeleteRequest): Flowable<Response<Void>> {
        return apiService.deletePoi(deleteRequest)
    }
    
    private fun getISODateTime() : String {
        val isoDatePattern = "yyyy-MM-dd'T'HH:mm:ssX"

        val simpleDateFormat = SimpleDateFormat(isoDatePattern, Locale.US)
        return simpleDateFormat.format(Calendar.getInstance().time)
    }
}
