package net.squanchy.proximity.preconditions;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;

import net.squanchy.support.lang.Optional;

import io.reactivex.Single;

class BluetoothPrecondition implements Precondition {

    private static final int REQUEST_ENABLE_BLUETOOTH = 8909;

    private final BluetoothManager bluetoothManager;
    private final TaskLauncher taskLauncher;

    BluetoothPrecondition(BluetoothManager bluetoothManager, TaskLauncher taskLauncher) {
        this.bluetoothManager = bluetoothManager;
        this.taskLauncher = taskLauncher;
    }

    @Override
    public boolean unavailable() {
        return bluetoothManager.getAdapter() == null;
    }

    @Override
    public boolean performsSynchronousSatisfiedCheck() {
        return CAN_PERFORM_SYNCHRONOUS_CHECK;
    }

    @Override
    public boolean satisfied() {
        BluetoothAdapter adapter = bluetoothManager.getAdapter();
        return adapter.isEnabled();
    }

    @Override
    public Single<SatisfyResult> satisfy() {
        return Single.create(emitter -> {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            taskLauncher.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
            emitter.onSuccess(SatisfyResult.WAIT_FOR_EXTERNAL_RESULT);
        });
    }

    @Override
    public Optional<Integer> requestCode() {
        return Optional.of(REQUEST_ENABLE_BLUETOOTH);
    }
}
