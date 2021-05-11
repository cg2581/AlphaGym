package com.ps12027_cuongnt.alphasupplements.model

class Version(val url: String, val name: String) {

    companion object {

        fun getList(): List<Version> {
            val versionList = ArrayList<Version>()
            versionList.clear()
            versionList.add(
                Version(
                    "Bánh trung thu",
                    "49.000 vnđ"
                )
            )
            versionList.add(
                Version(
                    "Bánh trung thu",
                    "49.000 vnđ"
                )
            )
            versionList.add(
                Version(
                    "Bánh trung thu",
                    "49.000 vnđ"
                )
            )
            versionList.add(
                Version(
                    "Bánh trung thu",
                    "49.000 vnđ"
                )
            )
            versionList.add(
                Version(
                    "Bánh trung thu",
                    "49.000 vnđ"
                )
            )
            return versionList
        }
    }
}