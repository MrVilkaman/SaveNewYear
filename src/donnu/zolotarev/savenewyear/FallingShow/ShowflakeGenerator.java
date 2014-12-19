package donnu.zolotarev.savenewyear.FallingShow;

import android.opengl.GLES20;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.emitter.IParticleEmitter;
import org.andengine.entity.particle.initializer.IParticleInitializer;
import org.andengine.entity.particle.modifier.IParticleModifier;
import org.andengine.opengl.util.GLState;
import org.andengine.util.Constants;
import org.andengine.util.math.MathUtils;

import java.util.ArrayList;

public class ShowflakeGenerator<T extends IEntity> extends Entity {

    private static final float[] POSITION_OFFSET_CONTAINER = new float[2];
    protected final IParticleEmitter mParticleEmitter;
    protected final ArrayList<IParticleInitializer<T>> mParticleInitializers = new ArrayList<IParticleInitializer<T>>();
    protected final ArrayList<IParticleModifier<T>> mParticleModifiers = new ArrayList<IParticleModifier<T>>();

    protected final float mRateMinimum;
    protected final float mRateMaximum;
    private final ArrayList<Particle> mParticles;
    private final ShowsPool particlesPool;

    private boolean mParticlesSpawnEnabled = true;
    private int mParticlesMaximum = 250;
    private float mParticlesDueToSpawn = 0;
    private boolean mBlendingEnabled = true;

    public  ShowflakeGenerator(IParticleEmitter mParticleEmitter, float pRateMinimum, float pRateMaximum) {
        this.mParticleEmitter = mParticleEmitter;
        this.mRateMinimum = pRateMinimum;
        this.mRateMaximum = pRateMaximum;
        this.mParticles = new ArrayList<Particle>();
        this.registerUpdateHandler(this.mParticleEmitter);
        particlesPool =  new ShowsPool();

    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(this.isParticlesSpawnEnabled()) {
            this.spawnParticles(pSecondsElapsed);
        }

        final int particleModifierCountMinusOne = this.mParticleModifiers.size() - 1;
        for(int i = this.mParticles.size() - 1; i >= 0; i--) {
            final Particle<T> particle = mParticles.get(i);

			/* Apply all particleModifiers */
            for(int j = particleModifierCountMinusOne; j >= 0; j--) {
                this.mParticleModifiers.get(j).onUpdateParticle(particle);
            }

            particle.onUpdate(pSecondsElapsed);
            if(particle.isExpired()){
                mParticles.remove(i);
                particlesPool.remoteUnit(particle);
                detachChild(particle.getEntity());
                /*this.moveParticleToEnd(i);*/
            }
        }
    }



    @Override
    protected void onManagedDraw(final GLState pGLState, final Camera pCamera) {
//        pGLState.enableBlend();
        pGLState.blendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        for(int i = this.mParticles.size() - 1; i >= 0; i--) {
            this.mParticles.get(i).onDraw(pGLState, pCamera);
        }
//        pGLState.disableBlend();
    }

    private void spawnParticles(final float pSecondsElapsed) {
        final float currentRate = this.determineCurrentRate();
        final float newParticlesThisFrame = currentRate * pSecondsElapsed;

        this.mParticlesDueToSpawn += newParticlesThisFrame;

        //final int particlesToSpawnThisFrame = Math.min(this.mParticlesMaximum - this.mParticles.size(), (int) FloatMath.floor(this.mParticlesDueToSpawn));
        int count = 0;

        for(int i = 0; i < mParticlesDueToSpawn; i++){
            this.spawnParticle();
            count++;
        }

        this.mParticlesDueToSpawn -= count;
    }

    private void spawnParticle() {
        if(!(this.mParticles.size() < this.mParticlesMaximum)) {
            Particle p = mParticles.get(0);
            detachChild(p.getEntity());
            particlesPool.remoteUnit(p);
            mParticles.remove(0);
        }
            Particle<T> particle = particlesPool.getUnit();

			/* New particle needs to be created. */
            this.mParticleEmitter.getPositionOffset(POSITION_OFFSET_CONTAINER);

            final float x = POSITION_OFFSET_CONTAINER[Constants.VERTEX_INDEX_X];
            final float y = POSITION_OFFSET_CONTAINER[Constants.VERTEX_INDEX_Y];

           /* if(particle == null) {
                particle = new Particle<T>();
                this.mParticles[this.mParticlesAlive] = particle;
                particle.setEntity(this.mEntityFactory.create(x, y));
            } else { */
        attachChild(particle.getEntity());
                mParticles.add(particle);
                particle.reset();
                particle.getEntity().setPosition(x, y);
        //    }

			/* Apply particle initializers. */
            {
                for(int i = this.mParticleInitializers.size() - 1; i >= 0; i--) {
                    this.mParticleInitializers.get(i).onInitializeParticle(particle);
                }

                for(int i = this.mParticleModifiers.size() - 1; i >= 0; i--) {
                    this.mParticleModifiers.get(i).onInitializeParticle(particle);
                }
            }

    }

    protected float determineCurrentRate() {
        if(this.mRateMinimum == this.mRateMaximum){
            return this.mRateMinimum;
        } else {
            return MathUtils.random(this.mRateMinimum, this.mRateMaximum);
        }
    }

    public boolean isParticlesSpawnEnabled() {
        return this.mParticlesSpawnEnabled;
    }

    public void addParticleModifier(final IParticleModifier<T> pParticleModifier) {
        this.mParticleModifiers.add(pParticleModifier);
    }

    public void removeParticleModifier(final IParticleModifier<T> pParticleModifier) {
        this.mParticleModifiers.remove(pParticleModifier);
    }

    public void addParticleInitializer(final IParticleInitializer<T> pParticleInitializer) {
        this.mParticleInitializers.add(pParticleInitializer);
    }

    public void removeParticleInitializer(final IParticleInitializer<T> pParticleInitializer) {
        this.mParticleInitializers.remove(pParticleInitializer);
    }
}
