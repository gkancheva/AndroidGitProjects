package com.gkancheva.multipanelayout;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FragmentTitles extends Fragment {

    public interface OnArticleSelectedListener {
        public void onArticleSelected(Article article);
    }

    public FragmentTitles() {
    }

    private OnArticleSelectedListener mListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Article> mArticles;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_titles, container, false);

        articlesStubImplementation();

        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_titles);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter(mArticles);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((RecyclerViewAdapter)mAdapter).setOnItemSelectedListener(new RecyclerViewAdapter.ISingleItemClickListener() {
            @Override
            public void onItemSelected(int position) {
                if(mListener != null) {
                    mListener.onArticleSelected(mArticles.get(position));
                }
            }
        });
    }

    //Called for Api < 23
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnArticleSelectedListener) activity;
    }

    //Called for Api >= 23
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnArticleSelectedListener) context;
    }

    private void articlesStubImplementation() {
        mArticles = new ArrayList<>();
        mArticles.add(new Article("Taking the final wrapper off of Android 7.0 Nougat", "Today, Android 7.0 Nougat will begin rolling out to users, starting with Nexus devices. At the same time, we’re pushing the Android 7.0 source code to the Android Open Source Project (AOSP), extending public availability of this new version of Android to the broader ecosystem.\n" +
                "\n" +
                "We’ve been working together with you over the past several months to get your feedback on this release, and also to make sure your apps are ready for the users who will run them on Nougat devices.\n" +
                "\n" +
                "What’s inside Nougat\n" +
                "Android Nougat reflects input from thousands of fans and developers like you, all around the world. There are over 250 major features in Android Nougat, including VR Mode in Android. We’ve worked at all levels of the Android stack in Nougat — from how the operating system reads sensor data to how it sends pixels to the display — to make it especially built to provide high quality mobile VR experiences.\n" +
                "\n" +
                "Plus, Nougat brings a number of new features to help make Android more powerful, more productive and more secure. It introduces a brand new JIT/AOT compiler to improve software performance, make app installs faster, and take up less storage. It also adds platform support for Vulkan, a low-overhead, cross-platform API for high-performance, 3D graphics. Multi-Window support lets users run two apps at the same time, and Direct Reply so users can reply directly to notifications without having to open the app. As always, Android is built with powerful layers of security and encryption to keep your private data private, so Nougat brings new features like File-based encryption, seamless updates, and Direct Boot.\n" +
                "\n" +
                "You can find all of the Nougat developer resources here, including details on behavior changes and new features you can use in your apps. An overview of what's new for developers is available here, and you can explore all of the new user features in Nougat here.\n" +
                "\n" +
                "Multi-window mode in Android Nougat\n" +
                "Multi-window mode in Android Nougat\n" +
                "The next wave of users\n" +
                "Starting today and rolling out over the next several weeks, the Nexus 6, Nexus 5X, Nexus 6P, Nexus 9, Nexus Player, Pixel C, and General Mobile 4G (Android One) will get an over-the-air software update to Android 7.0 Nougat. Devices enrolled in the Android Beta Program will also receive this final version.\n" +
                "\n" +
                "And there are many tasty devices coming from our partners running Android Nougat, including the upcoming LG V20, which will be the first new smartphone that ships with Android Nougat, right out of the box.\n" +
                "\n" +
                "With all of these new devices beginning to run Nougat, now is the time to publish your app updates to Google Play. We recommend compiling against, and ideally targeting, API 24. If you’re still testing some last minute changes, a great strategy to do this is using Google Play’s beta testing feature to get early feedback from a small group of users — including those using Android 7.0 Nougat — and then doing a staged rollout as you release the updated app to all users.\n" +
                "\n" +
                "What’s next for Nougat?\n" +
                "We’re moving Nougat into a new regular maintenance schedule over the coming quarters. In fact, we’ve already started work on the first Nougat maintenance release, that will bring continued refinements and polish, and we’re planning to bring that to you this fall as a developer preview. Stay tuned!\n" +
                "\n" +
                "We’ll be closing open bugs logged against Developer Preview builds soon, but please keep the feedback coming! If you still see an issue that you filed in the preview tracker, just file a new issue against Android 7.0 in the AOSP issue tracker."));
        mArticles.add(new Article("Connecting your App to a Wi-Fi Device", "With the growth of the Internet of Things, connecting Android applications to Wi-Fi enabled devices is becoming more and more common. Whether you’re building an app for a remote viewfinder, to set up a connected light bulb, or to control a quadcopter, if it’s Wi-Fi based you will need to connect to a hotspot that may not have Internet connectivity.\n" +
                "\n" +
                "From Lollipop onwards the OS became a little more intelligent, allowing multiple network connections and not routing data to networks that don’t have Internet connectivity. That’s very useful for users as they don’t lose connectivity when they’re near Wi-Fis with captive portals. Data routing APIs were added for developers, so you can ensure that only the appropriate app traffic is routed over the Wi-Fi connection to the external device.\n" +
                "\n" +
                "To make the APIs easier to understand, it is good to know that there are 3 sets of networks available to developers:\n" +
                "\n" +
                "WiFiManager#startScan returns a list of available Wi-Fi networks. They are primarily identified by SSID.\n" +
                "WiFiManager#getConfiguredNetworks returns a list of the Wi-Fi networks configured on the device, also indexed by SSID, but they are not necessarily currently available.\n" +
                "ConnectivityManager#getAllNetworks returns a list of networks that are being interacted with by the phone. This is necessary as from Lollipop onwards a device may be connected to multiple networks at once, Wi-Fi, LTE, Bluetooth, etc… The current state of each is available by calling ConnectivityManager#getNetworkInfo and is identified by a network ID.\n" +
                "In all versions of Android you start by scanning for available Wi-Fi networks with WiFiManager#startScan, iterate through the ScanResults looking for the SSID of your external Wi-Fi device. Once you’ve found it you can check if it is already a configured network using WifiManager#getConfiguredNetworks and iterating through the WifiConfigurations returned, matching on SSID. It’s worth noting that the SSIDs of the configured networks are enclosed in double quotes, whilst the SSIDs returned in ScanResults are not.\n" +
                "\n" +
                "If your network is configured you can obtain the network ID from the WifiConfiguration object. Otherwise you can configure it using WifiManager#addNetwork and keep track of the network id that is returned.\n" +
                "\n" +
                "To connect to the Wi-Fi network, register a BroadcastReceiver that listens for WifiManager.NETWORK_STATE_CHANGED_ACTION and then call WifiManager.enableNetwork (int netId, boolean disableOthers), passing in your network ID. The enableNetwork call disables all the other Wi-Fi access points for the next scan, locates the one you’ve requested and connects to it. When you receive the network broadcasts you can check with WifiManager#getConnectionInfo that you’re successfully connected to the correct network. But, on Lollipop and above, if that network doesn’t have internet connectivity network, requests will not be routed to it.\n" +
                "\n" +
                "Routing network requests\n" +
                "\n" +
                "To direct all the network requests from your app to an external Wi-Fi device, call ConnectivityManager#setProcessDefaultNetwork on Lollipop devices, and on Marshmallow call ConnectivityManager#bindProcessToNetwork instead, which is a direct API replacement. Note that these calls require android.permission.INTERNET; otherwise they will just return false.\n" +
                "\n" +
                "Alternatively, if you’d like to route some of your app traffic to the Wi-Fi device and some to the Internet over the mobile network:\n" +
                "\n" +
                "For HTTP requests you can use Network#openConnection(java.net.URL), directly routing your request to this network.\n" +
                "For low-level socket communication, open a socket and call Network#bindSocket(java.net.Socket), or alternatively use Network#getSocketFactory.\n" +
                "Now you can keep your users connected whilst they benefit from your innovative Wi-Fi enabled products."));
        mArticles.add(new Article("Strictly Enforced Verified Boot with Error Correction", "Overview\n" +
                "Android uses multiple layers of protection to keep users safe. One of these layers is verified boot, which improves security by using cryptographic integrity checking to detect changes to the operating system. Android has alerted about system integrity since Marshmallow, but starting with devices first shipping with Android 7.0, we require verified boot to be strictly enforcing. This means that a device with a corrupt boot image or verified partition will not boot or will boot in a limited capacity with user consent. Such strict checking, though, means that non-malicious data corruption, which previously would be less visible, could now start affecting process functionality more.\n" +
                "\n" +
                "By default, Android verifies large partitions using the dm-verity kernel driver, which divides the partition into 4 KiB blocks and verifies each block when read, against a signed hash tree. A detected single byte corruption will therefore result in an entire block becoming inaccessible when dm-verity is in enforcing mode, leading to the kernel returning EIO errors to userspace on verified partition data access.\n" +
                "\n" +
                "This post describes our work in improving dm-verity robustness by introducing forward error correction (FEC), and explains how this allowed us to make the operating system more resistant to data corruption. These improvements are available to any device running Android 7.0 and this post reflects the default implementation in AOSP that we ship on our Nexus devices.\n" +
                "\n" +
                "Error-correcting codes\n" +
                "Using forward error correction, we can detect and correct errors in source data by shipping redundant encoding data generated using an error-correcting code. The exact number of errors that can be corrected depends on the code used and the amount of space allocated for the encoding data.\n" +
                "\n" +
                "Reed-Solomon is one of the most commonly used error-correcting code families, and is readily available in the Linux kernel, which makes it an obvious candidate for dm-verity. These codes can correct up to ⌊t/2⌋ unknown errors and up to t known errors, also called erasures, when t encoding symbols are added.\n" +
                "\n" +
                "A typical RS(255, 223) code that generates 32 bytes of encoding data for every 223 bytes of source data can correct up to 16 unknown errors in each 255 byte block. However, using this code results in ~15% space overhead, which is unacceptable for mobile devices with limited storage. We can decrease the space overhead by sacrificing error correction capabilities. An RS(255, 253) code can correct only one unknown error, but also has an overhead of only 0.8%.\n" +
                "\n" +
                "\n" +
                "An additional complication is that block-based storage corruption often occurs for an entire block and sometimes spans multiple consecutive blocks. Because Reed-Solomon is only able to recover from a limited number of corrupted bytes within relatively short encoded blocks, a naive implementation is not going to be very effective without a huge space overhead.\n" +
                "\n" +
                "Recovering from consecutive corrupted blocks\n" +
                "In the changes we made to dm-verity for Android 7.0, we used a technique called interleaving to allow us to recover not only from a loss of an entire 4 KiB source block, but several consecutive blocks, while significantly reducing the space overhead required to achieve usable error correction capabilities compared to the naive implementation.\n" +
                "\n" +
                "Efficient interleaving means mapping each byte in a block to a separate Reed-Solomon code, with each code covering N bytes across the corresponding N source blocks. A trivial interleaving where each code covers a consecutive sequence of N blocks already makes it possible for us to recover from the corruption of up to (255 - N) / 2 blocks, which for RS(255, 223) would mean 64 KiB, for example.\n" +
                "\n" +
                "An even better solution is to maximize the distance between the bytes covered by the same code by spreading each code over the entire partition, thereby increasing the maximum number of consecutive corrupted blocks an RS(255, N) code can handle on a partition consisting of T blocks to ⌈T/N⌉ × (255 - N) / 2.\n" +
                "\n" +
                "\n" +
                "Interleaving with distance D and block size B.\n" +
                "\n" +
                "An additional benefit of interleaving, when combined with the integrity verification already performed by dm-verity, is that we can tell exactly where the errors are in each code. Because each byte of the code covers a different source block—and we can verify the integrity of each block using the existing dm-verity metadata—we know which of the bytes contain errors. Being able to pinpoint erasure locations allows us to effectively double our error correction performance to at most ⌈T/N⌉ × (255 - N) consecutive blocks.\n" +
                "\n" +
                "For a ~2 GiB partition with 524256 4 KiB blocks and RS(255, 253), the maximum distance between the bytes of a single code is 2073 blocks. Because each code can recover from two erasures, using this method of interleaving allows us to recover from up to 4146 consecutive corrupted blocks (~16 MiB). Of course, if the encoding data itself gets corrupted or we lose more than two of the blocks covered by any single code, we cannot recover anymore.\n" +
                "\n" +
                "While making error correction feasible for block-based storage, interleaving does have the side effect of making decoding slower, because instead of reading a single block, we need to read multiple blocks spread across the partition to recover from an error. Fortunately, this is not a huge issue when combined with dm-verity and solid-state storage as we only need to resort to decoding if a block is actually corrupted, which still is rather rare, and random access reads are relatively fast even if we have to correct errors.\n" +
                "\n" +
                "Conclusion\n" +
                "Strictly enforced verified boot improves security, but can also reduce reliability by increasing the impact of disk corruption that may occur on devices due to software bugs or hardware issues.\n" +
                "\n" +
                "The new error correction feature we developed for dm-verity makes it possible for devices to recover from the loss of up to 16-24 MiB of consecutive blocks anywhere on a typical 2-3 GiB system partition with only 0.8% space overhead and no performance impact unless corruption is detected. This improves the security and reliability of devices running Android 7.0."));
    }
}
