import { useRef, useEffect } from "react";

/**
 * Hook to run a callback once when the component is mounted.
 * @param callback Callback to run
 */
export const useRunningOnce = (callback) => {
    const runningRef = useRef(false);

    useEffect(() => {
        if (runningRef.current) return;
        runningRef.current = true;
        callback();
    }, [callback]);
};
