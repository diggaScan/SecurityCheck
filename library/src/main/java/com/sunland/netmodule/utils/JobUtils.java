package com.sunland.netmodule.utils;//package com.sunlandgroup.utils;
//
//import android.app.job.JobInfo;
//import android.app.job.JobScheduler;
//import android.content.ComponentName;
//import android.content.Context;
//import android.os.Build;
//
///**
// * Created by LSJ on 2017/10/25.
// */
//
//public class JobUtils {
//    /**
//     * 设定一个JobScheduler
//     * @param context
//     * @param cls   需要执行的组件
//     * @return
//     */
//    public static int schedule(Context context, Class<?> cls){
//        if (Build.VERSION.SDK_INT >= 21) {
//            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
//            scheduler.cancelAll();
//            ComponentName componentName = new ComponentName(context, cls);
//            JobInfo jobInfo = new JobInfo.Builder(1, componentName)
//                    //  .setMinimumLatency(5000)// 任务最少延迟时间
//                    //  .setOverrideDeadline(60000)// 任务deadline，当到期没达到指定条件也会开始执行
//                    .setPeriodic(1 * 60 * 1000)
//                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)// 网络条件，默认值NETWORK_TYPE_NONE
//                    .setRequiresCharging(true)// 是否充电
//                    //.setRequiresDeviceIdle(false)// 设备是否空闲
//                    .setPersisted(true) //设备重启后是否继续执行
//                    //.setBackoffCriteria(3000, JobInfo.BACKOFF_POLICY_LINEAR) //设置退避/重试策略
//                    .build();
//            return scheduler.schedule(jobInfo);
//        }
//        else
//            return -1;
//    }
//}
